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
 * HSF接口返回的状态对象中，包含状态码和状态信息。
 * <p/>
 * <p>
 * 便利接口，各项目中状态对象实现其接口，可以快速构建
 * {@link ResultBean}。
 * </p>
 *
 * @author puddor.Z at 2015/7/16 14:20
 */
public abstract class Result {

    public static GLOBAL GLOBAL = new GLOBAL();

    /**
     * 全局属性
     */
    public static class GLOBAL {
        public ResultCode FAIL = ResultCode.result(0, "操作失败，请重试操作或联系客服人员。");
        public ResultCode SUCCESS = ResultCode.result(1, "操作成功。");
        public ResultCode NO_DATA = ResultCode.result(400, "没有找到更多数据。");


        public ResultCode MISSING_PARAMS = ResultCode.result(-1, "您输入的数据不完整。");
        public ResultCode WRONG_PARAMS = ResultCode.result(-2, "您输入的数据有误。");
        public ResultCode DB_ERRORS = ResultCode.result(-4, "操作失败，请重试或联系客服人员。");
        public ResultCode TOO_MANY_RESULTS = ResultCode.result(-5, "返回结果超过一行记录。");
        public ResultCode ERROR_FORMAT = ResultCode.result(-6, "您输入的数据格式有误。");
        public ResultCode ERROR_FORWARD = ResultCode.result(-7, "错误页面跳转提示。");
        public ResultCode ERROR_JDBC = ResultCode.result(-8, "数据库级别错误。");
        public ResultCode ERROR_RPC = ResultCode.result(-9, "RPC调用失败。");
        public ResultCode INVALID_DATA = ResultCode.result(-10, "数据已失效或已伪删除。");
        public ResultCode UN_LOGIN = ResultCode.result(-11, "登录会话已过期，请重新登陆。");
        public ResultCode NO_PRIVILEGE_WATCH = ResultCode.result(-12, "对不起，你没全权限查看数据。");
        public ResultCode ONS_DUPLICATE_CONSUME = ResultCode.result(-13, "ONS重复消费。");
        public ResultCode REPEAT_DATA = ResultCode.result(-14, "数据已存在。");
        public ResultCode PROCESSING = ResultCode.result(-15, "业务正在处理中。");
        public ResultCode WRONG_PROCESSING_TIME = ResultCode.result(-16, "不在指定办公处理时间内。");
        public ResultCode PUSH_MESSAGE_TO_APP_FAILED = ResultCode.result(-17, "推送消息到App失败。");
        public ResultCode REPEAT_SUBMIT = ResultCode.result(-18, "请勿重复提交请求。");
        public ResultCode ASF_CHECKED = ResultCode.result(-19, "滑动验证码已过期，请重新验证。");
        public ResultCode NO_TRANSACTION_PWD = ResultCode.result(-20, "您的账户还未设置交易密码。");
        public ResultCode NO_SIGN_AUTHORIZSE_AGREEMENT = ResultCode.result(-21, "对不起，您未签署电子授权协议。");
    }


}
