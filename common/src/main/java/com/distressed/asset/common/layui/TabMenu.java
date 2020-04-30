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

import java.util.List;

/**
 * 采用Tab模板的菜单对象。顶部主菜单模块，左侧菜单列表，右侧菜单对应的内容页面；
 *
 * @author hongchao zhao at 2020-1-10 16:28
 */
public class TabMenu {
    //菜单名称
    private String title;
    //与顶部菜单的data-menu属性值相同，可使用菜单父节点ID
    private String menu1;
    //菜单前面的图标（可不填）
    private String icon;
    //对应的页面链接。（有子菜单的情况下建议不填）
    private String href;
    //0模块,1菜单,2按钮
    private Integer level;
    //子菜单是否展开。（默认不展开）
    private Boolean spread = Boolean.FALSE;
    //控制对应页面链接的打开方式。不设置的情况下以窗口形式打开，设置后页面整体跳转，如“登录页面”。可选参数：_blank。
    private String target;
    //子菜单数据。（格式同上）
    private List<TabMenu> children;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMenu1() {
        return menu1;
    }

    public void setMenu1(String menu1) {
        this.menu1 = menu1;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getSpread() {
        return spread;
    }

    public void setSpread(Boolean spread) {
        this.spread = spread;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<TabMenu> getChildren() {
        return children;
    }

    public void setChildren(List<TabMenu> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
