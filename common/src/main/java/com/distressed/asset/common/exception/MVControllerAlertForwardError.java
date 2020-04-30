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

/**
 * 定义MVC控制器例外，用于页面Javascript
 * 警告和页面重定向。
 *
 * 严重级别的异常，自定义异常，在Exceptionhandle捕获
 *
 * @author Yelin.G at 2015/12/13 17:28
 */
public class MVControllerAlertForwardError extends RuntimeException {

    protected String page;

    /**
     * 构建{@link MVControllerAlertForwardError}。
     */
    public MVControllerAlertForwardError(String message, String pageByLocation) {
        super(message);
        this.page = pageByLocation;
    }

    /**
     * 构建{@link MVControllerAlertForwardError}。
     */
    public MVControllerAlertForwardError(String message, Throwable throwable, String pageByLocation) {
        super(message, throwable);
        this.page = pageByLocation;
    }

    /**
     * {@link #page}的getter。
     */
    public String getPage() {
        return this.page;
    }
}
