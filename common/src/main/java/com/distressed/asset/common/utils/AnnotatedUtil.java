package com.distressed.asset.common.utils;

import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * ClassName: AnnotatedUtil
 * @author hongchao zhao
 * @since JDK 1.8
 */
public class AnnotatedUtil {
	
	public static <T extends Annotation> Map<Method, T> getMethodAndAnnotation(Object bean, final Class<T> annotation) {
		final Class<?> userType = bean.getClass();
		
	    Map<Method, T> methods = MethodIntrospector.selectMethods(userType,
			new MethodIntrospector.MetadataLookup<T>() {
			    @Override
				public T inspect(Method method) {
			        try {
			            return AnnotatedElementUtils.findMergedAnnotation(method, annotation);
			        } catch (Throwable ex) {
			            throw new IllegalStateException("Invalid mapping on handler class [" + userType.getName() + "]: " + method, ex);
			        }
			    }
			}
	    );
		return methods;
	}
	
}
