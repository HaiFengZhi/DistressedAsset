package com.distressed.asset.common.enums;

/**
 * @className bool枚举值
 * @description  判断bool 枚举值
 * @author wayne.yan
 * @date 2019/11/14
 * @version V1.0
 **/
public enum BoolEnum {

    YES(1, "是"),
    NO(0, "否");

    private int key;
    private String value;

    BoolEnum(int key, String value) {
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
     * 根据枚举key获取枚举
     *
     * @param
     *    @param key
     * @return com.distressed.asset.common.enums.BoolEnum
     * @author wayne.yan
     * @data 2019/11/14
     */
    public BoolEnum getSexByKey(int key) {
        for (BoolEnum sex : BoolEnum.values()) {
            if(sex.key == key){
                return sex;
            }
        }
        return null;
    }
}
