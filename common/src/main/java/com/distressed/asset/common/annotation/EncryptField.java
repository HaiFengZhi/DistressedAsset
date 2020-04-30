package com.distressed.asset.common.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @decription EncryptField
 * <p>字段加密注解</p>
 * @author hongchao zhao
 * @date 2017/10/24 13:01
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface EncryptField {
    String value() default "";
}
