package com.distressed.asset.portal.mapping;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色数据结构表。
 *
 * @author zhaohc
 * @date 2020-04-30
 */
public class Role implements Serializable {
    /**
     * 主键标识，自增。
     */
    private Long id;

    /**
     * 角色名称。
     */
    private String name;

    /**
     * 备注描述。
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 角色的状态: ‘1’有效；‘0’无效
     */
    private Boolean status;

    /**
     * admin_role
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
     * 角色名称。
     * @return name 角色名称。
     */
    public String getName() {
        return name;
    }

    /**
     * 角色名称。
     * @param name 角色名称。
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 角色的状态: ‘1’有效；‘0’无效
     * @return status 角色的状态: ‘1’有效；‘0’无效
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 角色的状态: ‘1’有效；‘0’无效
     * @param status 角色的状态: ‘1’有效；‘0’无效
     */
    public void setStatus(Boolean status) {
        this.status = status;
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