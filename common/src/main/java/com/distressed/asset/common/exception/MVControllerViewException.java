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

package com.distressed.asset.common.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义MVC控制器视图例外。
 *
 * <p>
 *     视图控制器例外，接收页面地址参数。页面地址可以是JSP页面，也可以是Velocity/Freemarker
 *     等模板页面，只要在MVC Framework中，比如说Spring MVC或者Struts2等，通过框架自有机制
 *     捕捉到这个例外之后，获取内部的页面进行渲染，保证例外的正常处理。
 * </p>
 *
 * 警告级别的异常，自定义异常，在Exceptionhandle捕获
 *
 * @author Yelin.G at 2015/08/04
 */
public class MVControllerViewException extends RuntimeException {

    /*缺省错误页面URL。*/
    protected String url = "/error";
    /*视图中可能需要的数据。*/
    protected Map<String, Object> model = new HashMap<String, Object>();

    /**
     * @see RuntimeException ( String )
     */
    public MVControllerViewException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException ( String )
     */
    public MVControllerViewException(String url, String message) {
        super(message);
        if(StringUtils.isNotBlank(url)) {
            this.url = url;
        }
    }

    /**
     * @see RuntimeException ( String )
     */
    public MVControllerViewException(String url, Map<String, Object> model, String message) {
        super(message);
        if(StringUtils.isNotBlank(url)) {
            this.url = url;
        }

        if(model != null) {
            this.model.putAll(model);
        }
    }

    /**
     * @see RuntimeException ( Throwable )
     */
    public MVControllerViewException(Throwable throwable) {
        super(throwable);
    }

    /**
     * @see RuntimeException ( Throwable )
     */
    public MVControllerViewException(String url, Throwable throwable) {
        super(throwable);
        if(StringUtils.isNotBlank(url)) {
            this.url = url;
        }
    }

    /**
     * @see RuntimeException ( Throwable )
     */
    public MVControllerViewException(String url, Map<String, Object> model, Throwable throwable) {
        super(throwable);
        if(StringUtils.isNotBlank(url)) {
            this.url = url;
        }

        if(model != null) {
            this.model.putAll(model);
        }
    }

    /**
     * {@link #url}的getter方法。
     */
    public final String getUrl() {
        return this.url;
    }

    /**
     * {@link #model}的getter方法。
     */
    public final Map<String, Object> getModel() {
        return this.model;
    }

    /**
     * {@link #model}的adder方法。
     */
    public final MVControllerViewException addModelAttribute(String key, Object value) {
        this.model.put(key, value);
        return this;
    }
}
