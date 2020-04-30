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

/**
 * 常用数据状态枚举。
 *
 * @author hongchao zhao at 2019-12-25 15:16
 */
public enum StatusEnum {
    NORMAL(0, "正常"),
    STOPPED(-1, "停用"),
    DELETION(-2, "删除"),
    ;
    private int key;
    private String value;

    StatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据枚举key获取枚举。
     *
     * @param key 键值。
     * @return 枚举对象。
     */
    public StatusEnum getStatusByKey(int key) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.key == key) {
                return status;
            }
        }
        return null;
    }

}
