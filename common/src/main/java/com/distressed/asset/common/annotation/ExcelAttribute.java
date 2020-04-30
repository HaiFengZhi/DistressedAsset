package com.distressed.asset.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *Excel配置注解自定义接口
 * @author zhaohc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface ExcelAttribute {
    /**
     * Excel中的列名
     *
     * @return
     */
    String name();

    /**
     * Excel中的列宽度
     *
     * @return
     */
    int width() default 20;

    /**
     * 列名对应的A,B,C,D...,不指定按照默认顺序排序
     *
     * @return
     */
    String column() default "";

    /**
     * 提示信息
     *
     * @return
     */
    String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容
     *
     * @return
     */
    String[] combo() default {};

    /**
     * 是否导出数据
     *
     * @return
     */
    boolean isExport() default true;

    /**
     * 是否为重要字段（整列标红,着重显示）
     *
     * @return
     */
    boolean isMark() default false;

    /**
     * 是否合计当前列(海量数据导出不支持)
     *
     * @return
     */
    boolean isSum() default false;

    /**
     * 金额是否格式化
     *
     * @return
     */
    boolean isMoney() default false;
    /**
     * 金额是数字类型
     *
     * @return
     */
    boolean isNumber() default false;
    /**
     * 数字类型可以进行排序
     *
     * @return
     */
    boolean isLong() default false;

    /**
     * 日期格式
     *
     * @return
     */
    String datePattern() default "yyyy-MM-dd HH:mm:ss";


    /**
     * 是否是日期数值
     *
     * @return
     */
    boolean isDateValue() default false;

    /**
     * 利率是百分比类型
     *
     * @return
     */
    boolean isRate() default false;
}

