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

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 中文拼音工具类。
 *
 * @author hongchao zhao at 2019-3-28 10:46
 */
public class ChinesePinyinUtils {

    /**
     * 得到 汉字的全拼
     *
     * @param src 中文字符串
     * @return
     */
    public static String getPingYin(String src) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder sb = new StringBuilder();
        char[] srcArray = src.toCharArray();
        try {
            for (int i = 0; i < srcArray.length; i++) {
                // 判断是否为汉字字符
                if (Character.toString(srcArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] targetArray = PinyinHelper.toHanyuPinyinStringArray(srcArray[i], format);
                    sb.append(targetArray[0]);
                } else {
                    sb.append(Character.toString(srcArray[i]));
                }
            }
            return sb.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 得到中文首字母,例如"专科"得到zk返回
     *
     * @param str 中文字符串
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                sb.append(pinyinArray[0].charAt(0));
            } else {
                sb.append(word);
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param cnStr 中文字符串
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuilder sb = new StringBuilder();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            sb.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String cnStr = "重庆,重视昭君发展(专科)环-境喵邈";
        System.out.println(getPingYin(cnStr));
        System.out.println(getPinYinHeadChar(cnStr));
        System.out.println(getCnASCII("专科"));

    }
}
