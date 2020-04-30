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

package com.distressed.asset.common.result;

import com.online.shop.common.annotation.DecryptField;
import com.online.shop.common.exception.ParameterException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * HSF接口响应对象定义。
 * <p> 
 *   去掉了success属性，前端需要判断code。
 *   当查询列表没有数据的时候，没有参数的时候统一用NO_DATA
 * </p>
 * @author puddor.Z at 2015/7/16 14:20
 *
 */
@SuppressWarnings({"rawtypes","unchecked" })
public class ResultBean<T>{

	/**
	 * 返回处理code，包括模块代码以及功能代码等信息，默认为0。
	 */
	private int code = Result.GLOBAL.SUCCESS.getCode();
	/**
	 * 返回处理状态说明，描述。
	 */
	private String message;
	/**
	 * 返回的业务数据。
	 */
	@DecryptField
	private T data;

    /**
     * 通过返回代码和返回信息构建{@link ResultBean}。
     *
     * @param code 返回代码。
     * @param message 返回信息。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> result(int code, String message) {
        ResultBean result = new ResultBean();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 通过{@link Result}实现对象和数据构建{@link ResultBean}。
     *
     * @param result {@link Result}。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> result(ResultCode result) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(result.getCode());
        resultBean.setMessage(result.getMessage());
        return resultBean;
    }

   /**
     * 通过{@link Result}实现对象和数据构建{@link ResultBean}。
     *
     * @param result {@link Result}。
     * @param data   数据。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> result(ResultCode result, T data) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(result.getCode());
        resultBean.setMessage(result.getMessage());
        resultBean.setData(data);
        return resultBean;
    }

	/**
	 * 构建成功响应的{@link ResultBean}对象。
	 *
	 * @return {@link ResultBean}。
	 */
	public static <T> ResultBean<T> success(){
		ResultBean result = new ResultBean();
        result.setCode(Result.GLOBAL.SUCCESS.getCode());
        result.setMessage(Result.GLOBAL.SUCCESS.getMessage());
		return result;
	}

    /**
     * 通过返回信息构建成功响应的{@link ResultBean}对象。
     *
     * @param message 返回信息。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> success(String message) {
        ResultBean result = new ResultBean();
        result.setMessage(message);
        result.setCode(Result.GLOBAL.SUCCESS.getCode());
        return result;
    }

    /**
     * 通过返回信息构建成功响应的{@link ResultBean}对象。
     *
     * @param message 返回信息。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> success(String message, T data) {
        ResultBean result = new ResultBean();
        result.setMessage(message);
        result.setCode(Result.GLOBAL.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    /**
     * 通过数据构建成功响应的{@link ResultBean}对象。
     *
     * @param data 返回数据。
     * @return {@link ResultBean}。
     */
	public static <T> ResultBean<T> successForData(T data){
		ResultBean<T> result = new ResultBean<T>();
		result.setMessage(Result.GLOBAL.SUCCESS.getMessage());
        result.setCode(Result.GLOBAL.SUCCESS.getCode());
		result.setData(data);
		return result;
	}

    /**
     * 通过返回错误信息构建失败响应的{@link ResultBean}。
     *
     * @param message 返回错误信息。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> failed(String message) {
        ResultBean result = new ResultBean();
        result.setCode(Result.GLOBAL.FAIL.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 通过返回代码和返回信息构建失败响应的{@link ResultBean}。
     *
     * @param code 返回代码。
     * @param message 返回信息。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> failed(int code, String message) {
        ResultBean result = new ResultBean();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 通过返回代码和返回信息构建失败响应的{@link ResultBean}。
     *
     * @param code 返回代码。
     * @param message 返回信息。
     * @param data 返回数据。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> failed(int code, String message, T data) {
        ResultBean result = new ResultBean();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 通过{@link Result}实现对象实例构建失败响应的{@link ResultBean}。
     *
     * @param error {@link Result}实现对象实例。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> failed(ResultCode error) {
        ResultBean result = new ResultBean();
        result.setCode(error.getCode());
        result.setMessage(error.getMessage());
        return result;
    }

    /**
     * 通过{@link Result}实现对象实例构建失败响应的{@link ResultBean}。
     *
     * @param error {@link Result}实现对象实例。
     * @return {@link ResultBean}。
     */
    public static <T> ResultBean<T> failed(ResultCode error, T data) {
        ResultBean result = new ResultBean();
        result.setCode(error.getCode());
        result.setMessage(error.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 响应失败，设置返回代码和返回信息，返回当前实例。
     *
     * @param code 返回代码。
     * @param message 返回信息。
     * @return {@link ResultBean}。
     */
    public ResultBean<T> setFailed(int code, String message){
		this.setCode(code);
		this.setMessage(message);
		return this;
	}

    /**
     * 响应失败，设置返回代码和返回信息，返回当前实例。
     *
     * @param error {@link Result}。
     * @return {@link ResultBean}。
     */
	public ResultBean<T> setFailed(ResultCode error){
		this.setCode(error.getCode());
		this.setMessage(error.getMessage());
		return this;
	}

    /**
     * {@link #success}的getter方法。
     * @return true：成功；false：失败。
     */
	public boolean isSuccess() {
		return code == Result.GLOBAL.SUCCESS.getCode();
	}

    /**
     * 建议用 setCode
     * {@link #success}的setter方法。
     *
     * @param success true：成功；false：失败。
     */
	public ResultBean<T> setSuccess(boolean success) {
		code = success ? Result.GLOBAL.SUCCESS.getCode() :Result.GLOBAL.FAIL.getCode();
        return this;
	}

    /**
     * {@link #code}的getter方法。
     *
     * @return 返回代码。
     */
	public int getCode() {
		return code;
	}

    /**
     * {@link #code}的setter方法。
     *
     * @param code 返回代码。
     */
	public ResultBean<T> setCode(int code) {
		this.code = code;
        return this;
	}

    /**
     * {@link #message}的getter方法。
     *
     * @return 返回信息。
     */
	public String getMessage() {
		return message;
	}

    /**
     * {@link #message}的setter方法。
     *
     * @param message 返回信息。
     */
	public ResultBean<T> setMessage(String message) {
		this.message = message;
        return this;
	}

    /**
     * {@link #data}的getter方法。
     *
     * @return 返回数据。
     */
	public T getData() {
		return data;
	}

    /**
     * {@link #data}的setter方法。
     *
     * @param data 返回数据。
     */
	public ResultBean<T> setData(T data) {
		this.data = data;
        return this;
	}

    /**
     * 拷贝{@link ResultBean}。
     *
     * @param source 源{@link ResultBean}。
     * @return {@link ResultBean}。
     */
    public ResultBean<T> clone(ResultBean<T> source) {
        if(source == null) {
            throw new ParameterException("Source must be not null.");
        }
        setCode(source.code);
        setData(source.data);
        setMessage(source.message);
        return this;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
