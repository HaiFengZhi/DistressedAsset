package com.distressed.asset.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 角色-权限数据结构关联表。
 *
 * @author zhaohc
 * @date 2020-04-30
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseRoleResourceDTO implements Serializable {
    /**
     * 关联角色表主键(T_ROLE)。
     */
    private Long roleId;

    /**
     * 关联权限表主键(T_PERMISSION)。
     */
    private Long resourceId;

    /**
     * admin_base_role_resource
     */
    private static final long serialVersionUID = 1L;

    /**
     * 关联角色表主键(T_ROLE)。
     * @return role_id 关联角色表主键(T_ROLE)。
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 关联角色表主键(T_ROLE)。
     * @param roleId 关联角色表主键(T_ROLE)。
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 关联权限表主键(T_PERMISSION)。
     * @return resource_id 关联权限表主键(T_PERMISSION)。
     */
    public Long getResourceId() {
        return resourceId;
    }

    /**
     * 关联权限表主键(T_PERMISSION)。
     * @param resourceId 关联权限表主键(T_PERMISSION)。
     */
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
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