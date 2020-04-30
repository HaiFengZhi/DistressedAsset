/*************************************************************************
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.common.utils;

/**
 * 全局常量类。
 *
 * @author hongchao zhao at 2019-11-14 13:36
 */
public class Constants {

    public final class Global {
        /**
         * session中存储随机验证码的key。
         */
        public static final String ATTR_SESSION_CAPTCHA = "RANDOM_SESSION_CODE";
        /**
         * Redis中存储随机验证码的key
         */
        public static final String PARAM_VERIFICATION_CODE = "_SSO_CODE_";
        /**
         * session中存储后台用户信息的key。
         */
        public static final String LOGIN_SESSION_USER_ADMIN = "LOGIN_SESSION_USER_ADMIN";
        /**
         * session中存储前端用户信息的key。
         */
        public static final String LOGIN_SESSION_USER = "LOGIN_SESSION_USER";

        /**
         * 后台用户头像缓存地址【包含了完整地址，空时有默认头像】
         */
        public static final String REDIS_ADMIN_USER_PORTRAIT = "REDIS_ADMIN_USER_PORTRAIT_";
        /**
         * 前台用户头像缓存地址【包含了完整地址，空时有默认头像】
         */
        public static final String REDIS_USER_PORTRAIT = "REDIS_USER_PORTRAIT_";



    }


}
