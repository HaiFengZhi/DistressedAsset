package com.distressed.asset.common.enums;

/**
 * 性别枚举
 *
 * @author wayne.yan
 * @data 2019/11/14
 */
public enum SexEnum {

    MEN(1, "男"),
    WOMEN(0, "女");

    private int key;
    private String value;

    SexEnum(int key, String value) {
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
     * 根据枚举key 获取枚举
     *
     * @param 
     *    @param key
     * @return com.online.shop.common.enums.SexEnum
     * @author wayne.yan
     * @data 2019/11/14       
     */
    public SexEnum getSexByKey(int key) {
        for (SexEnum sex : SexEnum.values()) {
            if(sex.key == key){
                return sex;
            }
        }
        return null;
    }
}
