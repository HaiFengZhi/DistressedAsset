package com.distressed.asset.common.layui;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * response返回类。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-10-29 23:26
 */
public class DTreeResponse {
    /** 状态码*/
    private int code;
    /** 信息标识*/
    private String msg;
    /** 状态类*/
    private Status status;
    /** 数据*/
    private Object data;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
