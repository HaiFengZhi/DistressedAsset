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
 * 消息例外，非运行时异常，强制捕获，不能因为消息出错影响主流业务。
 * 某些发送消息是主业务，需特殊处理一下。
 *
 * 找到所有捕捉这个例外的地方，严重级别，自定义异常，ExceptionHandler和监控
 *
 * @author Yelin.G at 2015/12/10 8:25
 */
public class MessageException extends Exception {

    /**
     * {@link RuntimeException()}。
     */
    public MessageException() {
        super();
    }

    /**
     * {@link RuntimeException(String)}。
     */
    public MessageException(String message) {
        super(message);
    }

    /**
     * {@link RuntimeException(Throwable)}。
     */
    public MessageException(Throwable throwable) {
        super(throwable);
    }
}
