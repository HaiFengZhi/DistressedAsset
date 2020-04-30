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

/**
 * HSF Service返回结果封装。
 *
 * @author Elvis.Huang at 2015/08/04 14:20
 */
public final class ResultCode {

    /*错误码。*/
    private int code;
    /*错误信息。*/
    private String message;

    public ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过<code>code</code>和<code>message</code>构建{@link ResultCode}。
     *
     * @param code 错误码。
     * @param message 错误信息。
     * @return {@link ResultCode}。
     */
    public static ResultCode result(int code, String message) {
        return new ResultCode(code, message);
    }

    /**
     * {@link #code}的getter。
     */
    public int getCode() {
        return code;
    }

    /**
     * {@link #code}的setter。
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * {@link #message}的getter。
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link #message}的setter。
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
