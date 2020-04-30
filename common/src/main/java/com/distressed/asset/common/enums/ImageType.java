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

/**
 * 网站图片类型枚举。
 *
 * @author hongchao zhao at 2020-1-14 10:12
 */
public enum ImageType {
    INDEX_BANNER(1, 1, "首页轮播图"),
    REGISTER_IMAGE(1, 2, "注册页面图"),
    LOGIN_IMAGE(1, 3, "登录页面图"),
    ;

    int website;
    int type;
    String description;

    ImageType(int website, int type, String description) {
        this.website = website;
        this.type = type;
        this.description = description;
    }

    /**
     * {@link #website}的getter方法。
     */
    public int getWebsite() {
        return website;
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
     * 通过{@link #type}转换为枚举类型。
     */
    public static ImageType valueOf(int type) {
        ImageType[] rts = ImageType.values();
        for (ImageType rt : rts) {
            if (rt.getType() == type) {
                return rt;
            }
        }
        throw new ParameterException(String.format("%d没有对应的%s。", type, ImageType.class));
    }

}
