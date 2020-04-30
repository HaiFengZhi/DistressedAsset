package com.distressed.asset.common.enums;

/**
 * 字典表分类模块枚举。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-11-25 21:02
 */
public enum KeywordsEnum {

    NORMAL("常用模块"),
    SYSTEM("系统模块"),
    PRODUCT("商品模块"),
    ORDER("订单模块"),
    USER("用户模块"),
    INFORMATION("信息模块"),
    OTHER("其它"),
    ;

    private String description;
    KeywordsEnum(String description){
        this.description = description;
    }

    public String getName(){
        return this.name();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
