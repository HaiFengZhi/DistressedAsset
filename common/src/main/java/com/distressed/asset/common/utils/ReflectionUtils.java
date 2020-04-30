/*************************************************************************
 *          HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *          COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS, IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 * DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *          HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;
import sun.reflect.Reflection;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * 反射类方法集合。
 *
 * @author Yelin.G at 2015/9/5 15:57
 */
public class ReflectionUtils {
    private static final Logger LOG = LogManager.getLogger(ReflectionUtils.class);

    private static final String EMPTY = "";

    private static final boolean SUN_REFLECTION_SUPPORTED;
    private static final Method GET_CALLER_CLASS;
    static final int JDK_7u25_OFFSET;
    private static final PrivateSecurityManager SECURITY_MANAGER;

    /**
     * 在获取方法的调用者时，不同的JDK版本拥有的特性决定了实现方式的不同。
     * 这里是检测各种JDK版本的特性，从而决定是否有更优化的方式获取方法的调用者。
     */
    static {
        Method getCallerClass;
        int java7u25CompensationOffset = 0;
        try {
            final Class<?> sunReflectionClass = ClassLoaderUtils.getThreadContextClassLoader$().loadClass("sun.reflect.Reflection");
            getCallerClass = sunReflectionClass.getDeclaredMethod("getCallerClass", int.class);
            Object o = getCallerClass.invoke(null, 0);
            final Object test1 = getCallerClass.invoke(null, 0);
            if (o == null || o != sunReflectionClass) {
                LOG.debug(ReflectionUtils.class.getSimpleName(), null, String.format("Unexpected return value from Reflection.getCallerClass(): %s", test1));
                getCallerClass = null;
                java7u25CompensationOffset = -1;
            } else {
                o = getCallerClass.invoke(null, 1);
                if (o == sunReflectionClass) {
                    LOG.debug(
                            ReflectionUtils.class.getSimpleName(), null,
                            "You are using Java 1.7.0_25 which has a broken implementation of Reflection.getCallerClass.");
                    LOG.debug(ReflectionUtils.class.getSimpleName(), null,"You should upgrade to at least Java 1.7.0_40 or later.");
                    LOG.debug(ReflectionUtils.class.getSimpleName(), null, "Using stack depth compensation offset of 1 due to Java 7u25.");
                    java7u25CompensationOffset = 1;
                }
            }
        } catch (final Exception e) {
            LOG.info(ReflectionUtils.class.getSimpleName(), null, "sun.reflect.Reflection.getCallerClass is not supported. " +
                    "ReflectionUtil.getCallerClass will be much slower due to this: " + e);
            getCallerClass = null;
            java7u25CompensationOffset = -1;
        }

        SUN_REFLECTION_SUPPORTED = getCallerClass != null;
        GET_CALLER_CLASS = getCallerClass;
        JDK_7u25_OFFSET = java7u25CompensationOffset;

        PrivateSecurityManager psm;
        try {
            final SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new RuntimePermission("createSecurityManager"));
            }
            psm = new PrivateSecurityManager();
        } catch (final SecurityException ignored) {
            LOG.info(ReflectionUtils.class.getSimpleName(), null,
                    "Not allowed to create SecurityManager. Falling back to slowest ReflectionUtil implementation.");
            psm = null;
        }
        SECURITY_MANAGER = psm;
    }

    private ReflectionUtils() {
        super();
    }

    /**
     * 测试当前的JDK版本是否支持{@link Reflection#getCallerClass(int)}。
     *
     * @return true：支持；false：不支持。
     */
    public static boolean supportsFastReflection() {
        return SUN_REFLECTION_SUPPORTED;
    }

    /**
     * 拿到调用{@link #getCallerClass(int)}这个方法的调用者，以{@link Class}返回。
     *
     * return Object.class instead of null (though it will have a null ClassLoader)，
     * (MS) I believe this would work without any modifications elsewhere, but I could be wrong
     * migrated from ReflectiveCallerClassUtility
     *
     * @param depth 栈深度。
     * @return 调用者。
     */
    public static Class<?> getCallerClass(final int depth) {
        if (depth < 0) {
            throw new IndexOutOfBoundsException(Integer.toString(depth));
        }
        // note that we need to add 1 to the depth value to compensate for this method, but not for the Method.invoke
        // since Reflection.getCallerClass ignores the call to Method.invoke()
        if (supportsFastReflection()) {
            try {
                return (Class<?>) GET_CALLER_CLASS.invoke(null, depth + 1 + JDK_7u25_OFFSET);
            } catch (final Exception e) {
                // theoretically this could happen if the caller class were native code
                LOG.error(ReflectionUtils.class.getSimpleName(), null, String.format("Error in ReflectionUtil.getCallerClass(%s).", depth));
                // TODO: return Object.class
                return null;
            }
        }
        // TODO: SecurityManager-based version?
        // slower fallback method using stack trace
        final StackTraceElement element = getEquivalentStackTraceElement(depth + 1);
        try {
            return ClassLoaderUtils.getThreadContextClassLoader$().loadClass(element.getClassName());
        } catch (final ClassNotFoundException e) {
            LOG.error(ReflectionUtils.class.getSimpleName(), null, String.format("Could not find class in ReflectionUtil.getCallerClass(%s).", depth));
        }
        // TODO: return Object.class
        return null;
    }

    /**
     * 获取调用者（包括调用的方法名和行号）。
     *
     * @param depth 栈深度。
     * @return 调用者路径。
     */
    public static String getCallerClassAndMethod(final int depth) {
        try {
            StackTraceElement element = getEquivalentStackTraceElement(depth);
            return element.getClassName() + "." + element.getMethodName() + ":" + element.getLineNumber();
        } catch (final IndexOutOfBoundsException e) {
            return "";
        }
    }

    /**
     * 获取调用者（包括调用的方法名和行号）。
     *
     * @param depth 栈深度。
     * @param separator 分隔符。
     * @return 调用者路径。
     */
    public static String getCallerClassAndMethod(final int depth, String separator) {
        try {
            StackTraceElement element = getEquivalentStackTraceElement(depth);
            return new StringBuilder(element.getClassName()).append(separator).append(element.getMethodName()).append(separator).append(element.getLineNumber()).toString();
        } catch (final IndexOutOfBoundsException e) {
            return "";
        }
    }

    /**
     * 获取调用者（包括调用的方法名和行号）。
     *
     * @param depth 栈深度。
     * @param separator 分隔符。
     * @return 调用者路径。
     */
    public static StringBuilder getCallerClassAndMethodAsBuffer(final int depth, String separator) {
        try {
            StackTraceElement element = getEquivalentStackTraceElement(depth);
            return new StringBuilder(element.getClassName()).append(separator).append(element.getMethodName()).append(separator).append(element.getLineNumber());
        } catch (final IndexOutOfBoundsException e) {
            return new StringBuilder();
        }
    }

    /**
     * 拿到调用{@link #getCallerClass(String)}这个方法的调用者，以{@link Class}返回。
     *
     * @param fqcn 类名。
     * @return 调用者。
     */
    public static Class<?> getCallerClass(final String fqcn) {
        return getCallerClass(fqcn, EMPTY);
    }

    /**
     * 拿到调用{@link #getCallerClass(String, String)}这个方法的调用者，以{@link Class}返回。
     *
     * @param fqcn 类名。
     * @param pkg 包名。
     * @return 调用者。
     */
    public static Class<?> getCallerClass(final String fqcn, final String pkg) {
        if (supportsFastReflection()) {
            boolean next = false;
            Class<?> clazz;
            for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
                if (fqcn.equals(clazz.getName())) {
                    next = true;
                    continue;
                }
                if (next && clazz.getName().startsWith(pkg)) {
                    return clazz;
                }
            }
            // TODO: return Object.class
            return null;
        }
        if (SECURITY_MANAGER != null) {
            return SECURITY_MANAGER.getCallerClass(fqcn, pkg);
        }
        try {
            return ClassLoaderUtils.getThreadContextClassLoader$().loadClass(getCallerClassName(fqcn, pkg, new Throwable().getStackTrace()));
        } catch (final ClassNotFoundException ignored) {
            // no problem really
        }
        // TODO: return Object.class
        return null;
    }

    /**
     * 拿到调用{@link #getCallerClass(Class)} 这个方法的调用者，以{@link Class}返回。
     *
     * @param anchor 父类名。
     * @return 调用者。
     */
    public static Class<?> getCallerClass(final Class<?> anchor) {
        if (supportsFastReflection()) {
            boolean next = false;
            Class<?> clazz;
            for (int i = 2; null != (clazz = getCallerClass(i)); i++) {
                if (anchor.equals(clazz)) {
                    next = true;
                    continue;
                }
                if (next) {
                    return clazz;
                }
            }
            return Object.class;
        }
        if (SECURITY_MANAGER != null) {
            return SECURITY_MANAGER.getCallerClass(anchor);
        }
        try {
            return ClassLoaderUtils.getThreadContextClassLoader$().loadClass(getCallerClassName(anchor.getName(), EMPTY,
                    new Throwable().getStackTrace()));
        } catch (final ClassNotFoundException ignored) {
            // no problem really
        }
        return Object.class;
    }

    /**
     * 辅助方法：拿到调用者类名。
     *
     * @param fqcn 类名。
     * @param pkg 包名。
     * @param elements {@link StackTraceElement}s。
     * @return 调用者类名。
     */
    private static String getCallerClassName(final String fqcn, final String pkg, final StackTraceElement... elements) {
        boolean next = false;
        for (final StackTraceElement element : elements) {
            final String className = element.getClassName();
            if (className.equals(fqcn)) {
                next = true;
                continue;
            }
            if (next && className.startsWith(pkg)) {
                return className;
            }
        }
        return Object.class.getName();
    }

    /**
     * 拿到当前调用堆栈。
     *
     * @return {@link Stack}。
     */
    public static Stack<Class<?>> getCurrentStackTrace() {
        // benchmarks show that using the SecurityManager is much faster than looping through getCallerClass(int)
        if (SECURITY_MANAGER != null) {
            final Class<?>[] array = SECURITY_MANAGER.getClassContext();
            final Stack<Class<?>> classes = new Stack<Class<?>>();
            classes.ensureCapacity(array.length);
            for (final Class<?> clazz : array) {
                classes.push(clazz);
            }
            return classes;
        }
        // slower version using getCallerClass where we cannot use a SecurityManager
        if (supportsFastReflection()) {
            final Stack<Class<?>> classes = new Stack<Class<?>>();
            Class<?> clazz;
            for (int i = 1; null != (clazz = getCallerClass(i)); i++) {
                classes.push(clazz);
            }
            return classes;
        }
        return new Stack<Class<?>>();
    }

    /**
     * 辅助类：实现{@link SecurityManager}。
     */
    static final class PrivateSecurityManager extends SecurityManager {

        @Override
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }

        protected Class<?> getCallerClass(final String fqcn, final String pkg) {
            boolean next = false;
            for (final Class<?> clazz : getClassContext()) {
                if (fqcn.equals(clazz.getName())) {
                    next = true;
                    continue;
                }
                if (next && clazz.getName().startsWith(pkg)) {
                    return clazz;
                }
            }
            // TODO: return Object.class
            return null;
        }

        protected Class<?> getCallerClass(final Class<?> anchor) {
            boolean next = false;
            for (final Class<?> clazz : getClassContext()) {
                if (anchor.equals(clazz)) {
                    next = true;
                    continue;
                }
                if (next) {
                    return clazz;
                }
            }
            return Object.class;
        }

    }

    /**
     * 辅助方法：模拟{@link Throwable}获取例外栈。
     *
     * @param depth 栈深度。
     * @return {@link StackTraceElement}。
     */
    static StackTraceElement getEquivalentStackTraceElement(final int depth) {
        // (MS) I tested the difference between using Throwable.getStackTrace() and Thread.getStackTrace(), and
        //      the version using Throwable was surprisingly faster! at least on Java 1.8. See ReflectionBenchmark.
        final StackTraceElement[] elements = new Throwable().getStackTrace();
        int i = 0;
        for (final StackTraceElement element : elements) {
            if (isValid(element)) {
                if (i == depth) {
                    return element;
                } else {
                    ++i;
                }
            }
        }
        LOG.error(ReflectionUtils.class.getSimpleName(), null, String.format("Could not find an appropriate StackTraceElement at index %s", depth));
        throw new IndexOutOfBoundsException(Integer.toString(depth));
    }

    /**
     * 辅助方法：判断例外栈中的元素是否有效。
     *
     * @param element {@link StackTraceElement}。
     * @return true：有效；false：无效。
     */
    private static boolean isValid(final StackTraceElement element) {
        // ignore native methods (oftentimes are repeated frames)
        if (element.isNativeMethod()) {
            return false;
        }
        final String cn = element.getClassName();
        // ignore OpenJDK internal classes involved with reflective invocation
        if (cn.startsWith("sun.reflect.")) {
            return false;
        }
        final String mn = element.getMethodName();
        // ignore use of reflection including:
        // Method.invoke
        // InvocationHandler.invoke
        // Constructor.newInstance
        if (cn.startsWith("java.lang.reflect.") && (mn.equals("invoke") || mn.equals("newInstance"))) {
            return false;
        }
        // ignore Class.newInstance
        if (cn.equals("java.lang.Class") && mn.equals("newInstance")) {
            return false;
        }
        // ignore use of Java 1.7+ MethodHandle.invokeFoo() methods
        if (cn.equals("java.lang.invoke.MethodHandle") && mn.startsWith("invoke")) {
            return false;
        }
        // any others?
        return true;
    }

    /**
     * Abstract class for testing different methods of getting the caller class name
     */
    private static abstract class GetCallerClassNameMethod {
        public abstract String getCallerClassName(int callStackDepth);
        public abstract String getMethodName();
    }

    /**
     * Uses the internal Reflection class
     */
    private static class ReflectionMethod extends GetCallerClassNameMethod {
        @Override
        public String getCallerClassName(int callStackDepth) {
            return Reflection.getCallerClass(callStackDepth).getName();
        }
        @Override
        public String getMethodName() {
            return "Reflection";
        }
    }

    /**
     * Get a stack trace from the current thread
     */
    private static class ThreadStackTraceMethod extends GetCallerClassNameMethod {
        @Override
        public String  getCallerClassName(int callStackDepth) {
            return Thread.currentThread().getStackTrace()[callStackDepth].getClassName();
        }
        @Override
        public String getMethodName() {
            return "Current Thread StackTrace";
        }
    }

    /**
     * Get a stack trace from a new Throwable
     */
    private static class ThrowableStackTraceMethod extends GetCallerClassNameMethod {
        @Override
        public String getCallerClassName(int callStackDepth) {
            return new Throwable().getStackTrace()[callStackDepth].getClassName();
        }

        @Override
        public String getMethodName() {
            return "Throwable StackTrace";
        }
    }

    /**
     * Use the SecurityManager.getClassContext()
     */
    private static class SecurityManagerMethod extends GetCallerClassNameMethod {
        @Override
        public String  getCallerClassName(int callStackDepth) {
            return mySecurityManager.getCallerClassName(callStackDepth);
        }
        @Override
        public String getMethodName() {
            return "SecurityManager";
        }

        /**
         * A custom security manager that exposes the getClassContext() information
         */
        static class MySecurityManager extends SecurityManager {
            public String getCallerClassName(int callStackDepth) {
                return getClassContext()[callStackDepth].getName();
            }
        }

        private final static MySecurityManager mySecurityManager = new MySecurityManager();
    }

    /*##############################对象内存测量##############################*/

    static Instrumentation inst;

    public static void prepare(String agentArgs, Instrumentation instP) {
        inst = instP;
    }

    public static long sizeOf(Object o) {
        if (inst == null) {
            throw new IllegalStateException("Can not access instrumentation environment.\n" +
                    "Please check if jar file containing SizeOfAgent class is \n" +
                    "specified in the java's \"-javaagent\" command line argument.");
        }
        return inst.getObjectSize(o);
    }

    /**
     * 递归计算当前对象占用空间总大小，包括当前类和超类的实例字段大小以及实例字段引用对象大小
     */
    public static long fullSizeOf(Object obj) {//深入检索对象，并计算大小
        Map<Object, Object> visited = new IdentityHashMap<Object, Object>();
        Stack<Object> stack = new Stack<Object>();
        long result = internalSizeOf(obj, stack, visited);
        while (!stack.isEmpty()) {//通过栈进行遍历
            result += internalSizeOf(stack.pop(), stack, visited);
        }
        visited.clear();
        return result;
    }

    //判定哪些是需要跳过的
    private static boolean skipObject(Object obj, Map<Object, Object> visited) {
        if (obj instanceof String) {
            if (obj == ((String) obj).intern()) {
                return true;
            }
        }
        return (obj == null) || visited.containsKey(obj);
    }

    private static long internalSizeOf(Object obj, Stack<Object> stack, Map<Object, Object> visited) {
        if (skipObject(obj, visited)) {//跳过常量池对象、跳过已经访问过的对象
            return 0;
        }
        visited.put(obj, null);//将当前对象放入栈中
        long result = 0;
        result += sizeOf(obj);
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {//如果数组
            if (clazz.getName().length() != 2) {// skip primitive type array
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    stack.add(Array.get(obj, i));
                }
            }
            return result;
        }
        return getNodeSize(clazz, result, obj, stack);
    }

    //这个方法获取非数组对象自身的大小，并且可以向父类进行向上搜索
    private static long getNodeSize(Class<?> clazz, long result, Object obj, Stack<Object> stack) {
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {//这里抛开静态属性
                    if (field.getType().isPrimitive()) {//这里抛开基本关键字（因为基本关键字在调用java默认提供的方法就已经计算过了）
                        continue;
                    } else {
                        field.setAccessible(true);
                        try {
                            Object objectToAdd = field.get(obj);
                            if (objectToAdd != null) {
                                stack.add(objectToAdd);//将对象放入栈中，一遍弹出后继续检索
                            }
                        } catch (IllegalAccessException ex) {
                            assert false;
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();//找父类class，直到没有父类
        }
        return result;
    }
    /*public static void main(String[] args) {
        *//*int[] arr = new int[64 * 1024 * 1024];

        for(int j = 0; j < 11; j++) {
            int K = (int)Math.pow(2, j);

            long start = System.currentTimeMillis();
            for (int i = 0; i < arr.length; i += K) {
                arr[i] *= 3;
            }
            System.out.println("K = [" + K + "]:" + (System.currentTimeMillis() - start));
        }*//*
        for(int j = 1; j <= 4 * 1024; j++) {
            int steps = j * 1024; // Arbitrary number of steps
            int[] arr = new int[steps];
            int lengthMod = arr.length - 1;
            long start = System.currentTimeMillis();
            for (int i = 0; i < steps; i++) {
                arr[(i * 16) & lengthMod]++; // (x & lengthMod) is equal to (x % arr.Length)
            }
            if(j == 1 || j== 16 || j == 256 || j == 512 || j == 1024 || j == (4 * 1024)) {
                System.out.println("steps = [" + j + "," + steps + "]:" + (System.currentTimeMillis() - start));
            }
        }
    }*/

    /*##############################包分析##############################*/

    /**
     * 包中查找符合条件{@link com.hongling.common.utils.ReflectionUtils.Test}的类和资源列表。
     *
     * @param test 条件{@link com.hongling.common.utils.ReflectionUtils.Test}的具体实现。
     * @param packageName 包名。
     * @param classMatches 符合条件的类集合。
     * @param resourceMatches 符合条件的资源列表。
     */
    public static void findInPackage(
            final Test test,
            String packageName,
            Set<Class<?>> classMatches,
            Set<URI> resourceMatches) {
        packageName = packageName.replace('.', '/');
        final ClassLoader loader = ClassLoaderUtils.getClassLoader$();
        Enumeration<URL> urls;

        try {
            urls = loader.getResources(packageName);
        } catch (final IOException ioe) {
            LOG.debug(ReflectionUtils.class.getSimpleName(), null, "读取包：" + packageName + "失败：" + ioe);
            return;
        }

        if(classMatches == null) {
            classMatches = new HashSet<Class<?>>();
        }

        if(resourceMatches == null) {
            resourceMatches = new HashSet<URI>();
        }
        while (urls.hasMoreElements()) {
            try {
                final URL url = urls.nextElement();
                final String urlPath = extractPath(url);

                LOG.info(ReflectionUtils.class.getSimpleName() + "在[" + urlPath + "] 检测类匹配条件： " + test);
                //考虑JBoss中war包的情况。
                if (VFSZIP.equals(url.getProtocol())) {
                    final String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
                    final URL newURL = new URL(url.getProtocol(), url.getHost(), path);
                    @SuppressWarnings("resource")
                    final JarInputStream stream = new JarInputStream(newURL.openStream());
                    try {
                        loadImplementationsInJar(test, packageName, path, stream, classMatches, resourceMatches);
                    } finally {
                        close(stream, newURL);
                    }
                } else if (BUNDLE_RESOURCE.equals(url.getProtocol())) {
                    loadImplementationsInBundle(test, packageName, classMatches, resourceMatches);
                } else {
                    final File file = new File(urlPath);
                    if (file.isDirectory()) {
                        loadImplementationsInDirectory(test, packageName, file, classMatches, resourceMatches);
                    } else {
                        loadImplementationsInJar(test, packageName, file, classMatches, resourceMatches);
                    }
                }
            } catch (final IOException ioe) {
                LOG.debug(ReflectionUtils.class.getSimpleName() + "无法读取entires：" + ioe);
            } catch (final URISyntaxException e) {
                LOG.debug(ReflectionUtils.class.getSimpleName() + "无法读取entires：" + e);
            }
        }
    }

    /**
     * 从{@link URL}中解析路径。
     *
     * @param url {@link URL}。
     * @return 路径。
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    private static String extractPath(final URL url) throws UnsupportedEncodingException, URISyntaxException {
        String urlPath = url.getPath(); // same as getFile but without the Query portion

        // I would be surprised if URL.getPath() ever starts with "jar:" but no harm in checking
        if (urlPath.startsWith("jar:")) {
            urlPath = urlPath.substring(4);
        }
        // For jar: URLs, the path part starts with "file:"
        if (urlPath.startsWith("file:")) {
            urlPath = urlPath.substring(6);
        }
        // If it was in a JAR, grab the path to the jar
        if (urlPath.indexOf('!') > 0) {
            urlPath = urlPath.substring(0, urlPath.indexOf('!'));
        }

        // Finally, decide whether to URL-decode the file name or not...
        final String protocol = url.getProtocol();
        final List<String> neverDecode = Arrays.asList(VFSZIP, BUNDLE_RESOURCE);
        if (neverDecode.contains(protocol)) {
            return urlPath;
        }
        final String cleanPath = new URI(urlPath).getPath();
        if (new File(cleanPath).exists()) {
            // if URL-encoded file exists, don't decode it
            return cleanPath;
        }
        return URLDecoder.decode(urlPath, "UTF-8");
    }

    /**
     * 从JAR文件中发现包含匹配包结构的目录结构的类和资源列表。如果不是一个JAR包文件
     * 或者文件不存在，不会扔出例外。
     *
     * @param test 测试条件{@link com.hongling.common.utils.ReflectionUtils.Test}用来过滤。
     * @param parent 父包。
     * @param stream JAR文件流。
     * @param classMatches 符合条件的类集合。
     * @param resourceMatches 符合条件的资源列表。
     */
    private static void loadImplementationsInJar(
            final Test test,
            final String parent,
            final String path,
            final JarInputStream stream,
            Set<Class<?>> classMatches,
            Set<URI> resourceMatches) {

        try {
            JarEntry entry;

            while ((entry = stream.getNextJarEntry()) != null) {
                final String name = entry.getName();
                if (!entry.isDirectory() && name.startsWith(parent) && isTestApplicable(test, name)) {
                    addIfMatching(test, name, classMatches, resourceMatches);
                }
            }
        } catch (final IOException ioe) {
            LOG.error(ReflectionUtils.class.getSimpleName() + "Could not search jar file '" + path + "' for classes matching criteria: " + test
                    + " due to an IOException: " + ioe);
        }
    }

    /**
     * 从JAR文件中发现包含匹配包结构的目录结构的类和资源列表。如果不是一个JAR包文件
     * 或者文件不存在，不会扔出例外。
     *
     * @param test 测试条件{@link com.hongling.common.utils.ReflectionUtils.Test}用来过滤。
     * @param parent 父包。
     * @param jarFile JAR文件。
     * @param classMatches 符合条件的类集合。
     * @param resourceMatches 符合条件的资源列表。
     */
    private static void loadImplementationsInJar(
            final Test test,
            final String parent,
            final File jarFile,
            Set<Class<?>> classMatches,
            Set<URI> resourceMatches) {
        @SuppressWarnings("resource")
        JarInputStream jarStream = null;
        try {
            jarStream = new JarInputStream(new FileInputStream(jarFile));
            loadImplementationsInJar(test, parent, jarFile.getPath(), jarStream, classMatches, resourceMatches);
        } catch (final FileNotFoundException ex) {
            LOG.error(ReflectionUtils.class.getSimpleName() + "Could not search jar file '" + jarFile + "' for classes matching criteria: " + test
                    + " file not found: " + ex);
        } catch (final IOException ioe) {
            LOG.error(ReflectionUtils.class.getSimpleName() + "Could not search jar file '" + jarFile + "' for classes matching criteria: " + test
                    + " due to an IOException: " + ioe);
        } finally {
            close(jarStream, jarFile);
        }
    }

    /**
     * 从OSGI文件中发现包含匹配包结构的目录结构的类和资源列表。
     *
     * @param test 测试条件{@link com.hongling.common.utils.ReflectionUtils.Test}用来过滤。
     * @param packageName 包名。
     * @param classMatches 符合条件的类集合。
     * @param resourceMatches 符合条件的资源列表。
     */
    private static void loadImplementationsInBundle(
            final Test test,
            final String packageName,
            Set<Class<?>> classMatches,
            Set<URI> resourceMatches) {
        final BundleWiring wiring = FrameworkUtil.getBundle(ReflectionUtils.class).adapt(BundleWiring.class);
        final Collection<String> list = wiring.listResources(packageName, "*.class",
                BundleWiring.LISTRESOURCES_RECURSE);
        for (final String name : list) {
            addIfMatching(test, name, classMatches, resourceMatches);
        }
    }

    /**
     * 在文件系统上查找物理目录，检测目录中的所有文件，如果文件对象不是一个目录并且以.class为结尾，会被
     * 测试是否满足{@link com.hongling.common.utils.ReflectionUtils.Test}，操作在目录中递归循环匹配符合包
     * 结构的文件。
     *
     * @param test 测试条件{@link com.hongling.common.utils.ReflectionUtils.Test}用来过滤。
     * @param parent 父目录。
     * @param location 文件对象位置。
     * @param classMatches 符合条件的类集合。
     * @param resourceMatches 符合条件的资源列表。
     */
    private static void loadImplementationsInDirectory(
            final Test test,
            final String parent,
            final File location,
            Set<Class<?>> classMatches,
            Set<URI> resourceMatches) {
        final File[] files = location.listFiles();
        if (files == null) {
            return;
        }

        StringBuilder builder;
        for (final File file : files) {
            builder = new StringBuilder();
            builder.append(parent).append('/').append(file.getName());
            final String packageOrClass = parent == null ? file.getName() : builder.toString();

            if (file.isDirectory()) {
                loadImplementationsInDirectory(test, packageOrClass, file, classMatches, resourceMatches);
            } else if (isTestApplicable(test, file.getName())) {
                addIfMatching(test, packageOrClass, classMatches, resourceMatches);
            }
        }
    }

    /**
     * 如果条件{@link com.hongling.common.utils.ReflectionUtils.Test}匹配，增加全路径类名和资源到
     * 集合列表中。
     *
     * @param test 测试条件{@link com.hongling.common.utils.ReflectionUtils.Test}用来过滤。
     * @param fqn 类全路径。
     * @param classMatches 符合条件的类集合。
     * @param resourceMatches 符合条件的资源列表。
     */
    protected static void addIfMatching(
            final Test test,
            final String fqn,
            Set<Class<?>> classMatches,
            Set<URI> resourceMatches) {
        try {
            final ClassLoader loader = ClassLoaderUtils.getClassLoader$();
            if (test.doesMatchClass()) {
                final String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
                LOG.debug(ReflectionUtils.class.getSimpleName() + "Checking to see if class " + externalName + " matches criteria [" + test + ']');

                final Class<?> type = loader.loadClass(externalName);
                if (test.matches(type)) {
                    classMatches.add(type);
                }
            }
            if (test.doesMatchResource()) {
                URL url = loader.getResource(fqn);
                if (url == null) {
                    url = loader.getResource(fqn.substring(1));
                }
                if (url != null && test.matches(url.toURI())) {
                    resourceMatches.add(url.toURI());
                }
            }
        } catch (final Throwable t) {
            LOG.debug(ReflectionUtils.class.getSimpleName() + "Could not examine class '" + fqn);
        }
    }

    /**
     * 测试文件名是否匹配{@link com.hongling.common.utils.ReflectionUtils.Test}条件。
     *
     * @param test 测试条件{@link com.hongling.common.utils.ReflectionUtils.Test}用来过滤。
     * @param path 文件名。
     * @return true：匹配；false：不匹配。
     */
    private static boolean isTestApplicable(final Test test, final String path) {
        return test.doesMatchResource() || path.endsWith(".class") && test.doesMatchClass();
    }

    /**
     * 关闭流。
     *
     * @param jarStream JAR流。
     * @param source 源。
     */
    private static void close(final JarInputStream jarStream, final Object source) {
        if (jarStream != null) {
            try {
                jarStream.close();
            } catch (final IOException e) {
                LOG.error(ReflectionUtils.class.getSimpleName() + String.format("Error closing JAR file stream for %s", source));
            }
        }
    }

    private static final String VFSZIP = "vfszip";
    private static final String BUNDLE_RESOURCE = "bundleresource";

    /**
     * 条件接口。
     *
     * <p>
     *     实现用于过滤包中文件的条件。
     * </p>
     */
    public interface Test<T> {
        /**
         * Will be called repeatedly with candidate classes. Must return True if a class is to be included in the
         * results, false otherwise.
         *
         * @param type 匹配的类型。
         * @return true：匹配；false：不匹配。
         */
        boolean matches(Class<T> type);

        /**
         * Test for a resource.
         *
         * @param resource 资源URI。
         * @return true：匹配；false：不匹配。
         */
        boolean matches(URI resource);

        /**
         * 是否仅匹配类？
         */
        boolean doesMatchClass();

        /**
         * 是否仅匹配资源？
         */
        boolean doesMatchResource();
    }
}