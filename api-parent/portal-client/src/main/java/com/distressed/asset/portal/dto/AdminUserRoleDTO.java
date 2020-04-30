package com.distressed.asset.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 用户-角色数据结构关联表。
 *
 * @author zhaohc
 * @date 2020-04-30
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserRoleDTO implements Serializable {
    /**
     * 关联用户表主键(T_USER)。
     */
    private Long userId;

    /**
     * 关联角色表主键(T_ROLE)。
     */
    private Long roleId;

    /**
     * admin_admin_user_role
     */
    private static final long serialVersionUID = 1L;

    /**
     * 关联用户表主键(T_USER)。
     * @return user_id 关联用户表主键(T_USER)。
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 关联用户表主键(T_USER)。
     * @param userId 关联用户表主键(T_USER)。
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

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