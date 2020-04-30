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
 * 同一用户重复登录例外。
 *
 * 警告级别的异常，自定义异常，ExceptionHandle和监控
 * @author ouchuanliang at 2015/11/30 21:00.
 */
public class DuplicateLoginException extends RuntimeException {

    /**
     * {@link RuntimeException()}。
     */
    public DuplicateLoginException() {
        super();
    }

    /**
     * {@link RuntimeException(String)}。
     */
    public DuplicateLoginException(String message) {
        super(message);
    }

    /**
     * {@link RuntimeException(Throwable)}。
     */
    public DuplicateLoginException(Throwable throwable) {
        super(throwable);
    }
}
