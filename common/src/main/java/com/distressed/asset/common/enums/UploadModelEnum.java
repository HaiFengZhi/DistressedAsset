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
 * 上传附件所属模块，用于区分不同的文件目录。
 *
 * @author hongchao zhao at 2019-11-19 16:28
 */
public enum UploadModelEnum {

    PORTRAIT("portrait","用户头像"),
    PRODUCT("product","商品图片"),
    EDITOR("editor","富文本编辑器图片"),
    OTHER("other","其它附件"),
    ;
    private String directory;
    private String value;

    UploadModelEnum(String directory, String value) {
        this.directory = directory;
        this.value = value;
    }

    public String getName(){
        return this.name();
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 根据文件目录获取上传模块枚举属性。
     *
     * @param name 名称
     * @return 模块枚举。
     */
    public static UploadModelEnum getValue(String name) {
        UploadModelEnum [] types = UploadModelEnum.values();
        for(UploadModelEnum modelEnum : types ){
            if(modelEnum.getName().equalsIgnoreCase(name)){
                return modelEnum;
            }
        }
        return UploadModelEnum.OTHER;
    }


}
