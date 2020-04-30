package com.distressed.asset.common.result;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 专门为layui返回对象。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-10-26 19:34
 */
@SuppressWarnings({"rawtypes","unchecked" })
public class LayUIResult<T> {

    /**
     * 返回处理code，包括模块代码以及功能代码等信息，默认为0。
     */
    private int code = 0;
    /**
     * 返回处理状态说明，描述。
     */
    private String msg;
    /**
     * 分页记录总条数。
     */
    private long count = 0;
    /**
     * 返回的业务数据。
     */
    private T data;

    /**
     * 通过返回信息构建成功响应的{@link ResultBean}对象。
     *
     * @return {@link ResultBean}。
     */
    public static <T> LayUIResult<T> success() {
        LayUIResult result = new LayUIResult();
        result.setMsg(Result.GLOBAL.SUCCESS.getMessage());
        result.setCode(0);
        return result;
    }

    /**
     * 通过返回信息构建成功响应的{@link ResultBean}对象。
     *
     * @param message 返回信息。
     * @return {@link ResultBean}。
     */
    public static <T> LayUIResult<T> success(String message) {
        LayUIResult result = new LayUIResult();
        result.setMsg(message);
        result.setCode(0);
        return result;
    }

    /**
     * 通过返回信息构建成功响应的{@link ResultBean}对象。
     *
     * @param message 返回信息。
     * @return {@link ResultBean}。
     */
    public static <T> LayUIResult<T> successForData(String message, T data) {
        LayUIResult result = new LayUIResult();
        result.setMsg(message);
        result.setCode(0);
        result.setData(data);
        return result;
    }

    /**
     * 通过数据构建成功响应的{@link ResultBean}对象。
     *
     * @param data 返回数据。
     * @return {@link ResultBean}。
     */
    public static <T> LayUIResult<T> successForData(T data){
        LayUIResult<T> result = new LayUIResult<T>();
        result.setMsg(Result.GLOBAL.SUCCESS.getMessage());
        result.setCode(0);
        result.setData(data);
        return result;
    }

    /**
     * 通过数据构建成功响应的{@link ResultBean}对象。
     *
     * @param data 返回数据。
     * @return {@link ResultBean}。
     */
    public static <T> LayUIResult<T> successForData(T data, long count){
        LayUIResult<T> result = new LayUIResult<T>();
        result.setMsg(Result.GLOBAL.SUCCESS.getMessage());
        result.setCode(0);
        result.setCount(count);
        result.setData(data);
        return result;
    }

    /**
     * 通过返回代码和返回信息构建失败响应的{@link LayUIResult}。
     *
     * @return {@link LayUIResult}。
     */
    public static <T> LayUIResult<T> failed() {
        LayUIResult result = new LayUIResult();
        result.setCode(-1);
        result.setMsg(Result.GLOBAL.FAIL.getMessage());
        return result;
    }

    /**
     * 通过返回代码和返回信息构建失败响应的{@link ResultBean}。
     *
     * @param message 返回信息。
     * @return {@link ResultBean}。
     */
    public static <T> LayUIResult<T> failed(String message) {
        LayUIResult result = new LayUIResult();
        result.setCode(-1);
        result.setMsg(message);
        return result;
    }

    /**
     * 通过返回代码和返回信息构建失败响应的{@link ResultBean}。
     *
     * @param code 返回代码。
     * @param message 返回信息。
     * @return {@link ResultBean}。
     */
    public static <T> LayUIResult<T> failed(int code, String message) {
        LayUIResult result = new LayUIResult();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
