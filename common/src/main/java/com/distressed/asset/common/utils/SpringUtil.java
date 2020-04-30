/*************************************************************************
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文工具类。
 *
 * @author hongchao zhao at 2019-12-9 16:48
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * Spring在bean初始化后会判断是不是ApplicationContextAware的子类
     * 如果该类是,setApplicationContext()方法,会将容器中ApplicationContext作为参数传入进去
     * @Author Sans
     * @CreateTime 2019/6/17 16:58
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (context == null){
            context = applicationContext;
        }
    }
    /**
     * 通过Name返回指定的Bean
     * @Author Sans
     * @CreateTime 2019/6/17 16:03
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

}
