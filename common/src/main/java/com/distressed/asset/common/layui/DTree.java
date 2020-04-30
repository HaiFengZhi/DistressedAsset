package com.distressed.asset.common.layui;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形信息类。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-10-29 23:24
 */
public class DTree {
    /** 节点ID*/
    private String id;
    /** 上级节点ID*/
    private String parentId;
    /** 节点名称*/
    private String title;
    /** 是否展开节点*/
    private Boolean spread;
    /** 是否最后一级节点*/
    private Boolean isLast;
    /** 节点禁用状态 **/
    private Boolean disabled;
    /** 自定义图标class*/
    private String iconClass;
    /** 表示用户自定义需要存储在树节点中的数据*/
    private Object basicData;
    /** 复选框集合*/
    private List<CheckArr> checkArr = new ArrayList<CheckArr>();
    /** 子节点集合*/
    private List<DTree> children = new ArrayList<DTree>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSpread() {
        return spread;
    }

    public void setSpread(Boolean spread) {
        this.spread = spread;
    }

    public Boolean getLast() {
        return isLast;
    }

    public void setLast(Boolean last) {
        isLast = last;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public Object getBasicData() {
        return basicData;
    }

    public void setBasicData(Object basicData) {
        this.basicData = basicData;
    }

    public List<CheckArr> getCheckArr() {
        return checkArr;
    }

    public void setCheckArr(List<CheckArr> checkArr) {
        this.checkArr = checkArr;
    }

    public List<DTree> getChildren() {
        return children;
    }

    public void setChildren(List<DTree> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
