package com.distressed.asset.common.layui;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 复选框设计类。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-10-29 23:16
 */
public class CheckArr {
    /** 复选框标记*/
    private String type;
    /** 复选框是否选中*/
    private String isChecked;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
