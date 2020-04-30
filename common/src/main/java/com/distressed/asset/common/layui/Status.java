package com.distressed.asset.common.layui;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 信息状态类。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-10-29 23:26
 */
public class Status {
    /** 状态码*/
    private int code = 200;
    /** 信息标识*/
    private String message = "success";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
