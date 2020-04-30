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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用的校验通用证件，号码等资源方法类集合。
 *
 * <p>
 *     一般来说，身份证，电话号码，邮件等验证。
 * </p>
 *
 * @author puddor.Z at 2015/07/20 10：05
 */
public final class ValidationUtils {

    private static final Pattern PATTERN_ID_CARD = Pattern.compile("^\\w{15}|\\w{18}$");
    //二代身份证
    private static final Pattern PATTERN_ID_CARD_V2 = Pattern.compile("^\\w{18}$");
    private static final Pattern PATTERN_CELLPHONE = Pattern.compile("^(1)\\d{10}$");
    private static final Pattern PATTERN_EMAIL = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private static final Pattern PATTERN_QQ = Pattern.compile("^\\d{5,11}");

    private static final Map<String, String> AREA_CODES_FOR_ID_CARD = new HashMap<String, String>(){{
        put("11", "北京");
        put("12", "天津");
        put("13", "河北");
        put("14", "山西");
        put("15", "内蒙古");
        put("21", "辽宁");
        put("22", "吉林");
        put("23", "黑龙江");
        put("31", "上海");
        put("32", "江苏");
        put("33", "浙江");
        put("34", "安徽");
        put("35", "福建");
        put("36", "江西");
        put("37", "山东");
        put("41", "河南");
        put("42", "湖北");
        put("43", "湖南");
        put("44", "广东");
        put("45", "广西");
        put("46", "海南");
        put("50", "重庆");
        put("51", "四川");
        put("52", "贵州");
        put("53", "云南");
        put("54", "西藏");
        put("61", "陕西");
        put("62", "甘肃");
        put("63", "青海");
        put("64", "宁夏");
        put("65", "新疆");
        put("71", "台湾");
        put("81", "香港");
        put("82", "澳门");
        put("91", "国外");
    }};

    private ValidationUtils() {
        super();
    }

    /**
     * 校验身份证件号码是否合法。
     *
     * @param idCard 证件号码。
     * @return true：合法；false：不合法。
     */
    public static boolean validateIdCardNumber(String idCard) {
        assert idCard != null;
        boolean matchOr = PATTERN_ID_CARD.matcher(idCard).matches();
        if(!matchOr) {
            return false;
        }
        // ================ 号码的长度 15位或18位 ================
        if (idCard.length() != 18) {
            return false;
        }

        String[] valCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String ai = idCard.substring(0, 17);

        // ================ 数字 除最后以为都为数字 ================
        if (!NumberUtils.isNumber(ai)) {
            return false;
        }
        // ================ 出生年月是否有效 ================
        String strYear = ai.substring(6, 10);       // 年份
        String strMonth = ai.substring(10, 12);     // 月份
        String strDay = ai.substring(12, 14);       // 月份
        if (!CommonUtils.isDateformat(strYear + "-" + strMonth + "-" + strDay)) {
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }

        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false;
        }
        // ================ 地区码时候有效 ================
        if (AREA_CODES_FOR_ID_CARD.get(ai.substring(0, 2)) == null) {
            return false;
        }
        // ================ 判断最后一位的值 ================
        int totalMulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            totalMulAiWi = totalMulAiWi + Integer.parseInt(String.valueOf(ai.charAt(i))) * Integer.parseInt(wi[i]);
        }
        int modValue = totalMulAiWi % 11;
        String strVerifyCode = valCodeArr[modValue];
        ai = ai + strVerifyCode;

        if (idCard.length() == 18) {
            if (!ai.equalsIgnoreCase(idCard)) {
                return false;
            }
        }
        return true;
    }
    /**
     * 校验身份证件号码是否合法 只匹配二代身份证。
     *
     * @param idCard 证件号码。
     * @return true：合法；false：不合法。
     */
    public static boolean validateIdCardNumberV2(String idCard) {
        assert idCard != null;
        boolean matchOr = PATTERN_ID_CARD_V2.matcher(idCard).matches();
        if(!matchOr) {
            return false;
        }

        String[] valCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String ai = idCard.substring(0, 17);

        // ================ 数字 除最后以为都为数字 ================
        if (!NumberUtils.isNumber(ai)) {
            return false;
        }
        // ================ 出生年月是否有效 ================
        String strYear = ai.substring(6, 10);       // 年份
        String strMonth = ai.substring(10, 12);     // 月份
        String strDay = ai.substring(12, 14);       // 月份
        if (!CommonUtils.isDateformat(strYear + "-" + strMonth + "-" + strDay)) {
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }

        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false;
        }
        // ================ 地区码时候有效 ================
        if (AREA_CODES_FOR_ID_CARD.get(ai.substring(0, 2)) == null) {
            return false;
        }
        // ================ 判断最后一位的值 ================
        int totalMulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            totalMulAiWi = totalMulAiWi + Integer.parseInt(String.valueOf(ai.charAt(i))) * Integer.parseInt(wi[i]);
        }
        int modValue = totalMulAiWi % 11;
        String strVerifyCode = valCodeArr[modValue];
        ai = ai + strVerifyCode;

        if (idCard.length() == 18) {
            if (!ai.equalsIgnoreCase(idCard)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验身份证件出生日期是否满足平台的18岁限制。
     * 18岁前隔一天
     * @param idCard 证件号码。
     * @return true：18岁；false：不满18岁。
     */
    public static boolean isOlderThanEighteen(String idCard) {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.YEAR,-18);
        Date maxDate=calendar.getTime();
       if( MyDateUtils.getIntervalByDays(maxDate, MyDateUtils.parseString(idCard.substring(6,14)+" 00:00:00","yyyyMMdd HH:mm:ss"))>-1){
           return false;
       }else{
           return true;
       }
    }






    /**
	 * 校验手机号码是否有效。
	 * 
	 * @param phoneNumber 手机号码。
	 * @return true：合法；false：不合法。
	 */
	public static boolean validatePhoneNumber(String phoneNumber) {
        return PATTERN_CELLPHONE.matcher(phoneNumber).matches();
	}

	/**
	 * 校验Email地址是否合法。
     *
	 * @param email 邮件地址。
	 * @return true：合法；false：不合法。
	 */
	public static boolean validateEmail(String email){
        return PATTERN_EMAIL.matcher(email).matches();
	}

	/**
	 * 校验QQ号码是否有效。
     *
	 * @param qqAccount QQ号。
	 * @return true：合法；false：不合法。
	 */
	public static boolean validateQQAccount(String qqAccount){
        return PATTERN_QQ.matcher(qqAccount).matches();
	}

    /**
     * 处理用户名，显示第一个和最后一个。
     *
     * @param userName 待处理的用户名。
     * @return 处理后的用户名。
     */
    public static String handleUserName(String userName) {

        if (null != userName && userName.length()>2){
            userName = userName.substring(0, 1) + appendWildCard(userName.length()-2)+userName.charAt(userName.length()-1);
            return userName;
        } else {
            return "**";
        }
    }

    /**
     * 生成 N 个通配符。
     *
     * @param length 通配符的个数。
     * @return 通配符字符串。
     */
    private static String appendWildCard(int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
            s += "*";
        }
        return s;
    }

    /**
     * 处理用户的电话号码，中间五个数字变*，如果不是手机号码返回四个*。
     *
     * @param phoneNumber 待处理的用户手机号码。
     * @return 处理后的电话号码。
     */
    public static String handlePhone(String phoneNumber) {
        if (ValidationUtils.validatePhoneNumber(phoneNumber)) {
            phoneNumber = phoneNumber.substring(0, 3) + appendWildCard(5) + phoneNumber.substring(8, phoneNumber.length());
            return phoneNumber;
        }
        return "****";
    }

    /**
     * 处理用户名含有手机号码
     *
     * @param loginUserName 待处理用户名。
     * @return 处理后的电话号码。
     */
    public static String handleLoginUserName(String loginUserName) {
        if(null==loginUserName||"".equals(loginUserName)){
            return loginUserName;
        }
        Pattern p = Pattern.compile("[0-9]{11}");
        Matcher m = p.matcher(loginUserName);
        boolean flag= false;
        int start=0;
        int end=0;
        String newStr="";
        while(m.find()) {
            flag=true;
            start=m.start();
            end=m.end();
        }
        if(flag){
            String phoneStr= loginUserName.substring(start,end);
            //注释掉否则匹配正则处理有问题，比如13825682568叠号会有问题
//            String regex = phoneStr.substring(3,7);
//            newStr=str.replaceAll(regex,"****");
            String phoneStr1 =phoneStr.substring(0,3);
            String phoneStr2 =phoneStr.substring(7,11);
            String str1 = loginUserName.substring(0,start);
            String str2 = loginUserName.substring(end,loginUserName.length());
            newStr = str1+phoneStr1+"****"+phoneStr2+str2;
            return newStr;
        }
        return loginUserName;
    }

    /**
     * 显示银行卡号前三4位,隐藏银行卡号的后四位。
     *
     * @param bankCardNumber 银行卡号。
     * @return 隐藏的银行卡号。
     */
    public static String handleBankCard(String bankCardNumber) {
        if (StringUtils.isEmpty(bankCardNumber)) {
            return bankCardNumber;
        }
        if (bankCardNumber.length() > 4) {
            String s = bankCardNumber.substring(0,4);
            String hidStr =s+appendWildCard(bankCardNumber.length() - 7);
            return hidStr + bankCardNumber.substring(bankCardNumber.length() - 4, bankCardNumber.length());
        }
        return bankCardNumber;
    }

    /**
     * 处理用户的姓名，除了第一个字其他都变*。如果不是用户名返回三个*。
     *
     * @param name 待处理的用户名。
     * @return 处理后的用户名。
     */
    public static String handleName(String name) {
        if (StringUtils.isNotBlank(name) && validateRealName(name) && name.length() > 0) {
            name = name.substring(0, 1) + appendWildCard(name.length() - 1);
        } else {
            name = "***";
        }
        return name;
    }

    /**
     * 校验用户名真实姓名是否有效正确。
     *
     * @param name 用户真实姓名。
     * @return true：有效；false：无效。
     */
    public static boolean validateRealName(String  name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        return true;
    }

    /**
     * 处理用户的身份证件号码，第四个个数字3位数不变其他变为*，如果不是身份证件号码返回四个*。
     *
     * @param idCardNumber 待处理的用户身份证件号码。
     * @return 处理后的用户身份证件号码。
     */
    public static String handleIdCardNumber(String idCardNumber) {
        if (idCardNumber != null && idCardNumber.length() >= 7) {
            idCardNumber = appendWildCard(4) + idCardNumber.substring(4, 7) + appendWildCard(idCardNumber.length() - 7);
            return idCardNumber;
        } else if (idCardNumber != null && idCardNumber.length() < 7) {
            return idCardNumber;
        } else {
            return "****";
        }
    }
    /**
     * 处理用户的真实姓名，第一个*。
     *
     * @param realName 待处理的用户真实姓名。
     * @return 处理后的用户真实姓名。
     */
    public static String handleRealName(String realName) {

        if (null != realName && realName.length()>1 && ValidationUtils.validateRealName(realName)) {
            realName = "*" + realName.substring(1, 2) + (realName.length() > 2 ? "*" : "");
            return realName;
        } else {
            return "***";
        }
    }


    /**
     * 处理用户的邮箱，转换成形如 1****2@qq.com 的方式，如果不是邮箱返回四个*。
     *
     * @param email 邮箱。
     * @return 处理后的邮箱。
     */
    public static String handleEmail(String email) {
        if (ValidationUtils.validateEmail(email)) {
            return email.substring(0, 1) + appendWildCard(email.indexOf("@") - 2) + email.substring(email.indexOf("@") - 1);
        }
        return "****";
    }

    /**
     * 校验用户名称是否有效正确。
     *
     * @param name 用户名。
     * @return true：有效；false：无效。
     */
    public static boolean validateLoginName(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        if (name.length() < 2 || name.length() > 16) {
            return false;
            //return ResultBean.result(name.UC_USERNAME_ILLEGAL.getCode(),"用户名的长度只能在 5 ~ 16 个字符之间！",false);
        }
        //检测用户名是否存在以下非法词汇及非法字符：
        //投资宝 (包含繁体)、红岭(包含繁体)、touzibao、hongling 、tzbao、my089
//        String reg="^(?!.*(红岭|投资宝|紅嶺|投資寶|周世平|touzibao|tzbao|hongling|my089|创投|兼职|客服|信贷|代理|渠道|公司)).*$";
//        if(name.matches(reg)){
//            return false;
//        }
//        reg="^[a-zA-Z0-9_\u4e00-\u9fa5]+$";
//        if(!name.matches(reg)){
//            return false;
//        }
        return true;
    }
}
