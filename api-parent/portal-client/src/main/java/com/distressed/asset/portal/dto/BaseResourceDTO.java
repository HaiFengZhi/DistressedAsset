package com.distressed.asset.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * 后台权限数据列表
 *
 * @author zhaohc
 * @date 2020-04-30
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResourceDTO implements Serializable {
    /**
     * 主键标识，自增。
     */
    private Long id;

    /**
     * 权限简短名称。
     */
    private String name;

    /**
     * 权限对应的访问地址。
     */
    private String accessUrl;

    /**
     * 权限呈树状菜单，因此一个权限可能附属于在父权限之下；根权限为空。
     */
    private Long parentId;

    /**
     * 备注描述。
     */
    private String description;

    /**
     * 0模块,1菜单,2按钮
     */
    private Integer level;

    /**
     * 是否作为菜单显示？0不显示 1显示
     */
    private Boolean showOr;

    /**
     * 菜单排序值
     */
    private Integer sequence;

    /**
     * 是否为主权限，0=否，1=是
     */
    private Boolean mainPermissionsOr;

    /**
     * 是否激活，0-有效，-1-失效
     */
    private Integer status;

    /**
     * 子节点。
     */
    private List<BaseResourceDTO> children;

    /**
     * 是否默认选中
     * true: 选中 false: 未选中
     */
    private Boolean checked;

    /**
     * admin_base_resource
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键标识，自增。
     * @return id 主键标识，自增。
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键标识，自增。
     * @param id 主键标识，自增。
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 权限简短名称。
     * @return name 权限简短名称。
     */
    public String getName() {
        return name;
    }

    /**
     * 权限简短名称。
     * @param name 权限简短名称。
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 权限对应的访问地址。
     * @return access_url 权限对应的访问地址。
     */
    public String getAccessUrl() {
        return accessUrl;
    }

    /**
     * 权限对应的访问地址。
     * @param accessUrl 权限对应的访问地址。
     */
    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl == null ? null : accessUrl.trim();
    }

    /**
     * 权限呈树状菜单，因此一个权限可能附属于在父权限之下；根权限为空。
     * @return parent_id 权限呈树状菜单，因此一个权限可能附属于在父权限之下；根权限为空。
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 权限呈树状菜单，因此一个权限可能附属于在父权限之下；根权限为空。
     * @param parentId 权限呈树状菜单，因此一个权限可能附属于在父权限之下；根权限为空。
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 备注描述。
     * @return description 备注描述。
     */
    public String getDescription() {
        return description;
    }

    /**
     * 备注描述。
     * @param description 备注描述。
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 0模块,1菜单,2按钮
     * @return level 0模块,1菜单,2按钮
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 0模块,1菜单,2按钮
     * @param level 0模块,1菜单,2按钮
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 是否作为菜单显示？0不显示 1显示
     * @return show_or 是否作为菜单显示？0不显示 1显示
     */
    public Boolean getShowOr() {
        return showOr;
    }

    /**
     * 是否作为菜单显示？0不显示 1显示
     * @param showOr 是否作为菜单显示？0不显示 1显示
     */
    public void setShowOr(Boolean showOr) {
        this.showOr = showOr;
    }

    /**
     * 菜单排序值
     * @return sequence 菜单排序值
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * 菜单排序值
     * @param sequence 菜单排序值
     */
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    /**
     * 是否为主权限，0=否，1=是
     * @return main_permissions_or 是否为主权限，0=否，1=是
     */
    public Boolean getMainPermissionsOr() {
        return mainPermissionsOr;
    }

    /**
     * 是否为主权限，0=否，1=是
     * @param mainPermissionsOr 是否为主权限，0=否，1=是
     */
    public void setMainPermissionsOr(Boolean mainPermissionsOr) {
        this.mainPermissionsOr = mainPermissionsOr;
    }

    /**
     * 是否激活，0-有效，-1-失效
     * @return status 是否激活，0-有效，-1-失效
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 是否激活，0-有效，-1-失效
     * @param status 是否激活，0-有效，-1-失效
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<BaseResourceDTO> getChildren() {
        return children;
    }

    public void setChildren(List<BaseResourceDTO> children) {
        this.children = children;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object target) {
        return EqualsBuilder.reflectionEquals(this, target, false);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(17, 37, this);
    }

}