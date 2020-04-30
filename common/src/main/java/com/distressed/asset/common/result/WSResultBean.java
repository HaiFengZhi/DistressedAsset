/*************************************************************************
 *                  online.shop CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) online.shop CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY online.shop CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * online.shop CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF online.shop CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 * DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  online.shop CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.common.result;

/**
 * 针对才平台的ResultBean。
 *
 * @author LanBin Lai at 2015/9/9 16:18
 */
public class WSResultBean<T> {

    /**
     * 返回处理成功或者失败，默认false。
     */
    private int success;
    /**
     * 返回处理code，包括模块代码以及功能代码等信息，默认为0。
     */
    private int code;
    /**
     * 返回处理状态说明，描述。
     */
    private String message;
    /**
     * 返回的业务数据。
     */
    private T data;

    /**
     * 通过{@link com.online.shop.common.result.WSResultBean#message}构建{@link com.online.shop.common.result.WSResultBean}。
     *
     * @param message 返回信息。
     * @return {@link com.online.shop.common.result.WSResultBean}。
     */
    public static <T> WSResultBean<T> failed(String message) {
        WSResultBean result = new WSResultBean();
        result.setSuccess(0);
        result.setMessage(message);
        result.setData("");
        return result;
    }


    /**
     * 构建成功响应的{@link WSResultBean}对象。
     *
     * @return {@link com.online.shop.common.result.WSResultBean}。
     */
    public static <T> WSResultBean<T> success() {
        WSResultBean result = new WSResultBean();
        result.setSuccess(1);
        result.setMessage("");
        result.setCode(1);
        result.setData("");
        return result;
    }

    /**
     * 通过{@link com.online.shop.common.result.WSResultBean#message}构建成功响应的{@link WSResultBean}对象。
     *
     * @param message 返回信息。
     * @return {@link com.online.shop.common.result.WSResultBean}。
     */
    public static <T> WSResultBean<T> success(String message) {
        WSResultBean result = new WSResultBean();
        result.setSuccess(1);
        result.setCode(1);
        result.setMessage(message);
        result.setData("");
        return result;
    }

    /**
     * 通过{@link com.online.shop.common.result.WSResultBean#data}构建成功响应的{@link WSResultBean}对象。
     *
     * @param data 返回数据。
     * @return {@link com.online.shop.common.result.WSResultBean}。
     */
    public static <T> WSResultBean<T> successForData(T data) {
        WSResultBean<T> result = new WSResultBean<T>();
        result.setSuccess(1);
        result.setMessage("");
        result.setData(data);
        result.setCode(1);
        return result;
    }

    protected WSResultBean() {
        super();
    }

    /**
     * {@link #success}的getter方法。
     */
    public int getSuccess() {
        return success;
    }

    /**
     * {@link #success}的setter方法。
     */
    public void setSuccess(int success) {
        this.success = success;
    }

    /**
     * {@link #code}的getter方法。
     */
    public int getCode() {
        return code;
    }

    /**
     * {@link #code}的setter方法。
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * {@link #message}的getter方法。
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link #message}的setter方法。
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * {@link #data}的getter方法。
     */
    public T getData() {
        return data;
    }

    /**
     * {@link #data}的setter方法。
     */
    public void setData(T data) {
        this.data = data;
    }
}
