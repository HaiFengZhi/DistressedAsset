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

package com.distressed.asset.common.enums;


import com.distressed.asset.common.exception.ParameterException;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 证件类型枚举。
 *
 * @author hongchao zhao at 2019-12-4 10:53
 */
public enum CertificateType {
    //数据库已存在的脏数据
    EXPIRED(0, "未知类型"),
    ID_CARD(1, "身份证"),
    OFFICER_CARD(2, "军官证"),
    ID_CARD_TAIWAN(3, "台胞证"),
    ID_CARD_HONGKONG(4, "香港地区身份证"),
    PASS_ID_TO_MAINLAND(5, "港澳居民来往内地通行证"),
    CORPORATIONAL_LICENSE(6, "企业营业执照"),
    BOOK_RESIDENT(7, "户口本"),
    PASSPORT(8, "护照"),
    TEMPORARY_ID_CARD(9, "临时身份证");
    private int type;
    private String description;

    CertificateType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * {@link #type}的getter方法。
     */
    public int getType() {
        return type;
    }

    /**
     * {@link #description}的getter方法。
     */
    public String getDescription() {
        return description;
    }

    /**
     * 通过{@link #type}转换为{@link #description}。
     */
    public static String getDescription(int type) {
        CertificateType[] cts = CertificateType.values();
        for (CertificateType ct : cts) {
            if (ct.getType() == type) {
                return ct.getDescription();
            }
        }

        throw new ParameterException(String.format("%d没有对应的%s。", type, CertificateType.class));
    }

    /**
     * 通过{@link #type}转换为枚举类型。
     */
    public static CertificateType valueOf(int type) {
        CertificateType[] cts = CertificateType.values();
        for (CertificateType ct : cts) {
            if (ct.getType() == type) {
                return ct;
            }
        }
        throw new ParameterException(String.format("%d没有对应的%s。", type, CertificateType.class));
    }

    /**
     * 以枚举值-名称注释的方式转换成{@link Map}。
     *
     * @return 以枚举值-名称注释的方式转换成的{@link Map}。
     */
    public static Map<String, String> toMap() {
        Map<String, String> result = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                //升序排序
                return obj1.compareTo(obj2);
            }
        });
        CertificateType[] vs = CertificateType.values();
        for (CertificateType one : vs) {
            result.put(one.getType() + "", one.getDescription());
        }
        return result;
    }

}
