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
 * 数据更新或者删除影响的行数不匹配造成的例外。
 *
 * 严重级别的异常，自定义异常，在Exceptionhandle和监控捕获
 * @author Yelin.G at 2015.08/08
 */
public class RowsUpdateNotMatchException extends RuntimeException {

    /**
     * {@link RuntimeException()}。
     */
    public RowsUpdateNotMatchException() {
        super();
    }

    /**
     * {@link RuntimeException(String)}。
     */
    public RowsUpdateNotMatchException(String message) {
        super(message);
    }

    /**
     * {@link RuntimeException(Throwable)}。
     */
    public RowsUpdateNotMatchException(Throwable throwable) {
        super(throwable);
    }
}
