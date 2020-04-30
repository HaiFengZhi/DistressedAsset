/*************************************************************************
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 * DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.common.utils;

import org.apache.logging.log4j.util.LoaderUtil;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * {@link ClassLoader}魔法工具箱。
 *
 * <p>
 * JVM类加载体系中有三种ClassLoader：Boostrap ClassLoader，
 * Extension ClassLoader以及System ClassLoader。
 * </p>
 *
 * <p>
 * 在某些情况下，我们想要获取加载某个类的{@link ClassLoader}
 * 实现，这里的方法就是你想要的。
 * </p>
 *
 * @author Yelin.G at 2015/09/04 15:44
 * @author Vladimir Roubtsov论ClassLoader的解释和分享
 */
public final class ClassLoaderUtils {

	/*######################################## 高级应用 ########################################*/

	private static IClassLoadStrategy s_strategy; // initialized in <clinit>

	private static final int CALL_CONTEXT_OFFSET = 2; // may need to change if this class is redesigned
	private static final CallerResolver CALLER_RESOLVER; // set in <clinit> private static Throwable CLINIT_FAILURE;

	static {
		CallerResolver temp = null;
		try {
			// this can fail if the current SecurityManager does not allow
			// RuntimePermission ("createSecurityManager"):

			temp = new CallerResolver();
		} catch (Throwable t) {
			//CLINIT_FAILURE = t;
		}
		CALLER_RESOLVER = temp;

		s_strategy = new DefaultClassLoadStrategy();
	}

	/**
	 * 不能对外暴漏。
	 */
	private ClassLoaderUtils() {
		super();
	}

	/**
	 * This method selects the best classloader instance to be used for
	 * class/resource loading by whoever calls this method. The decision
	 * typically involves choosing between the caller's current, thread context,
	 * system, and other classloaders in the JVM and is made by the
	 * {@link IClassLoadStrategy} instance established by the last call to
	 * {@link #setStrategy}.
	 *
	 * @return classloader to be used by the caller ['null' indicates the primordial loader]
	 */
	public static synchronized ClassLoader getClassLoader(final Class caller) {
		final ClassLoadContext ctx = new ClassLoadContext(caller);
		return s_strategy.getClassLoader(ctx);
	}

	/**
	 * This method selects the "best" classloader instance to be used for
	 * class/resource loading by whoever calls this method. The decision
	 * typically involves choosing between the caller's current, thread context,
	 * system, and other classloaders in the JVM and is made by the {@link IClassLoadStrategy}
	 * instance established by the last call to {@link #setStrategy}.<P>
	 * <p/>
	 * This method uses its own caller to set the call context. To be able to
	 * override this decision explicitly, use {@link #getClassLoader(Class)}.<P>
	 * <p/>
	 * This method does not throw.
	 *
	 * @return classloader to be used by the caller ['null' indicates the
	 * primordial loader]
	 */
	public static synchronized ClassLoader getClassLoader() {
		final Class caller = getCallerClass(1); // 'caller' can be set to null
		final ClassLoadContext ctx = new ClassLoadContext(caller);
		return s_strategy.getClassLoader(ctx);
	}

	/*
	 * Indexes into the current method call context with a given offset. Offset 0
	 * corresponds to the immediate caller, offset 1 corresponds to its caller,
	 * etc.<P>
	 *
	 * Invariant: getCallerClass(0) == class containing code that performs this call
	 */
	public static Class getCallerClass(final int callerOffset) {
		if (CALLER_RESOLVER == null) return null; // only happens if <clinit> failed
		return CALLER_RESOLVER.getClassContext()[CALL_CONTEXT_OFFSET + callerOffset];
	}

	/**
	 * Returns 'true' if 'loader2' is a delegation child of 'loader1' [or if
	 * 'loader1'=='loader2']. Of course, this works only for classloaders that
	 * set their parent pointers correctly. 'null' is interpreted as the
	 * primordial loader [i.e., everybody's parent].
	 */
	public static boolean isChild(final ClassLoader loader1, ClassLoader loader2) {
		if (loader1 == loader2) {
			return true;
		}
		if (loader2 == null) {
			return false;
		}
		if (loader1 == null) {
			return true;
		}

		for (; loader2 != null; loader2 = loader2.getParent()) {
			if (loader2 == loader1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the current classloader selection strategy setting.
	 */
	public static synchronized IClassLoadStrategy getStrategy() {
		return s_strategy;
	}

	/**
	 * Sets the classloader selection strategy to be used by subsequent calls
	 * to {@link #getClassLoader()}. An instance of {@link DefaultClassLoadStrategy}
	 * is in effect if this method is never called.
	 *
	 * @param strategy new strategy [may not be null]
	 * @return previous setting
	 */
	public static synchronized IClassLoadStrategy setStrategy(final IClassLoadStrategy strategy) {
		if (strategy == null) {
			throw new IllegalArgumentException("null input: strategy");
		}
		final IClassLoadStrategy old = s_strategy;
		s_strategy = strategy;
		return old;
	}

	/**
	 * A helper class to get the call context. It subclasses SecurityManager to
	 * make getClassContext() accessible. An instance of CallerResolver only
	 * needs to be created, not installed as an actual security manager.
	 */
	private static final class CallerResolver extends SecurityManager {
		@Override
		protected Class[] getClassContext() {
			return super.getClassContext();
		}
	}

	public interface IClassLoadStrategy {
		ClassLoader getClassLoader(ClassLoadContext ctx);
	}

	private static final class DefaultClassLoadStrategy implements IClassLoadStrategy {
		@Override
		public ClassLoader getClassLoader(final ClassLoadContext ctx) {
			if (ctx == null) {
				throw new IllegalArgumentException("null input: ctx");
			}

			final Class caller = ctx.getCallerClass();
			final ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();

			ClassLoader result;

			if (caller == null) {
				result = contextLoader;
			} else {
				final ClassLoader callerLoader = caller.getClassLoader();

				// if 'callerLoader' and 'contextLoader' are in a parent-child
				// relationship, always choose the child:

				// SF BUG 975080: change the sibling case to use 'callerLoader'
				// to work around ANT 1.6.x incorrect classloading model:

				if (isChild(callerLoader, contextLoader)) {
					result = contextLoader;
				} else {
					result = callerLoader;
				}
			}

			final ClassLoader systemLoader = ClassLoader.getSystemClassLoader();

			// precaution for when deployed as a bootstrap or extension class:
			if (isChild(result, systemLoader)) {
				result = systemLoader;
			}

			return result;
		}
	}

	public static class ClassLoadContext {
		private final Class m_caller;

		/**
		 * Returns the class representing the caller of {@link com.hongling.common.utils.ClassLoaderUtils}
		 * API. Can be used to retrieve the caller's classloader etc (which may be
		 * different from the ClassLoaderResolver's own classloader) ['null' if caller
		 * resolver could be instantiated due to security restrictions].
		 */
		public final Class getCallerClass() {
			return m_caller;
		}

		/**
		 * This constructor is package-private to restrict instantiation to
		 * {@link com.hongling.common.utils.ClassLoaderUtils} only.
		 *
		 * @param caller [can be null]
		 */
		ClassLoadContext(final Class caller) {
			m_caller = caller;

		}
	}

	/*######################################## 普通应用 ########################################*/

	private static final boolean GET_CLASS_LOADER_DISABLED;
	private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
	private static final PrivilegedAction<ClassLoader> TCCL_GETTER = new ThreadContextClassLoaderGetter$();

	static {
		if (SECURITY_MANAGER != null) {
			boolean getClassLoaderDisabled;
			try {
				SECURITY_MANAGER.checkPermission(new RuntimePermission("getClassLoader"));
				getClassLoaderDisabled = false;
			} catch (final SecurityException ignored) {
				getClassLoaderDisabled = true;
			}
			GET_CLASS_LOADER_DISABLED = getClassLoaderDisabled;
		} else {
			GET_CLASS_LOADER_DISABLED = false;
		}
	}


	/**
	 * 获取{@link ClassLoader}。
	 *
	 * @return {@link ClassLoader}。
	 */
	public static ClassLoader getClassLoader$() {
		return getClassLoader$(ClassLoaderUtils.class, null);
	}

	/**
	 * 通过两个{@link Class}交叉获取可用的{@link ClassLoader}。
	 *
	 * @param class1 {@link Class}。
	 * @param class2 {@link Class}。
	 * @return {@link ClassLoader}。
	 */
	private static ClassLoader getClassLoader$(final Class<?> class1, final Class<?> class2) {
		final ClassLoader threadContextClassLoader = getThreadContextClassLoader$();
		final ClassLoader loader1 = class1 == null ? null : class1.getClassLoader();
		final ClassLoader loader2 = class2 == null ? null : class2.getClassLoader();

		if (isChild$(threadContextClassLoader, loader1)) {
			return isChild$(threadContextClassLoader, loader2) ? threadContextClassLoader : loader2;
		}
		return isChild$(loader1, loader2) ? loader1 : loader2;
	}

	/**
	 * 获取当前线程的{@link ClassLoader}，如果不存在，使用应用ClassLoader。
	 *
	 * @return {@link ClassLoader}。
	 * @see LoaderUtil#getThreadContextClassLoader()
	 */
	public static ClassLoader getThreadContextClassLoader$() {
		if (GET_CLASS_LOADER_DISABLED) {
			// we can at least get this class's ClassLoader regardless of security context
			// however, if this is null, there's really no option left at this point
			return LoaderUtil.class.getClassLoader();
		}
		return SECURITY_MANAGER == null
				? TCCL_GETTER.run()
				: AccessController.doPrivileged(TCCL_GETTER);
	}

	/**
	 * 如果系统权限打开，通过这种方式获取{@link ClassLoader}。
	 */
	private static class ThreadContextClassLoaderGetter$ implements PrivilegedAction<ClassLoader> {
		@Override
		public ClassLoader run() {
			final ClassLoader cl = Thread.currentThread().getContextClassLoader();
			if (cl != null) {
				return cl;
			}
			final ClassLoader ccl = LoaderUtil.class.getClassLoader();
			return ccl == null && !GET_CLASS_LOADER_DISABLED ? ClassLoader.getSystemClassLoader() : ccl;
		}
	}

	/**
	 * 决定是否一个{@link ClassLoader}是否是另一个{@link ClassLoader}的孩子，注意
	 * 一个<code>null</code>的{@link ClassLoader}被解释为应用{@link ClassLoader}。
	 *
	 * @param loader1 the ClassLoader to check for childhood。
	 * @param loader2 the ClassLoader to check for parenthood。
	 * @return {@code true} if the first ClassLoader is a strict descendant of the second ClassLoader。
	 */
	private static boolean isChild$(final ClassLoader loader1, final ClassLoader loader2) {
		if (loader1 != null && loader2 != null) {
			ClassLoader parent = loader1.getParent();
			while (parent != null && parent != loader2) {
				parent = parent.getParent();
			}
			// once parent is null, we're at the system CL, which would indicate they have separate ancestry
			return parent != null;
		}
		return loader1 != null;
	}
}
