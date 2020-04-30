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

package com.distressed.asset.common.layui;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 多重下拉返回对象【不搞花哨，就搞个三重下拉】。
 *
 * @author hongchao zhao at 2019-12-17 16:54
 */
public class MultipleSelect {
    /**
     * 下拉实际值
     */
    private String value;
    /**
     * 下拉显示值
     */
    private String text;
    /**
     * 下级列表
     */
    private List<MultipleSelect> children = new ArrayList<>();

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<MultipleSelect> getChildren() {
        return children;
    }

    public void setChildren(List<MultipleSelect> children) {
        this.children = children;
    }

    /**
     * 将数据封装成下拉对象。
     *
     * @param value 实际值。
     * @param text  显示值。
     * @return 下拉对象。
     */
    public static MultipleSelect from(String value, String text) {
        MultipleSelect select = new MultipleSelect();
        select.setValue(value);
        select.setText(text);
        return select;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
