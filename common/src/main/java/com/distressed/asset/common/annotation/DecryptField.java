package com.distressed.asset.common.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @decription DecryptField
 * <p>字段解密注解</p>
 *
 * @author hongchao zhao
 * @date 2017/10/24 13:05
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface DecryptField {
    String value() default "";
}
