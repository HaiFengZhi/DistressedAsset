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
 * 缓存例外。
 *
 * <p>
 *     定义当和缓存服务器交互操作时发生的异常。
 * </p>
 *
 * 严重级别的错误，自定义异常，ExceptionHandle和监控
 * @author Yelin.G at 2015/07/28
 */
public class CacheException extends RuntimeException {

    /**
     * @see RuntimeException(String)
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * @see RuntimeException(Throwable)
     */
    public CacheException(Throwable throwable) {
        super(throwable);
    }
}
