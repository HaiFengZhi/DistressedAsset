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
package com.distressed.asset.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author WangXu at 2017/3/29 15:18
 */
public class VerifyCodeUtils {
    public static final char[] VERIFY_CODES = new char[]{'2','3','4','5','6','7','8','9',
            'w','e','r','y','u','p','a','s','d','g','h','k','x','c','v','b','n','m',
            'Q','W','E','R','T','Y','U','P','A','S','D','F','G','H','K','X','C','V','B','N','M'};

    /**
     * 生成验证码
     * @param length 验证码长度
     * @return
     */
    public static String random(int length){
         return RandomStringUtils.random(length,VERIFY_CODES);
    }
}