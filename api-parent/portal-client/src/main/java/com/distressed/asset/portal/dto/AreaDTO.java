package com.distressed.asset.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 省市区表
 *
 * @author zhaohc
 * @date 2020-04-30
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaDTO implements Serializable {
    /**
     * 地区编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 上级编码
     */
    private String parentCode;

    /**
     * 
     */
    private Byte root;

    /**
     * 
     */
    private Integer zone;

    /**
     * 状态
     */
    private Integer status;

    /**
     * t_area
     */
    private static final long serialVersionUID = 1L;

    /**
     * 地区编码
     * @return code 地区编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 地区编码
     * @param code 地区编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 名称
     * @return name 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 上级编码
     * @return parent_code 上级编码
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 上级编码
     * @param parentCode 上级编码
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode == null ? null : parentCode.trim();
    }

    /**
     * 
     * @return root 
     */
    public Byte getRoot() {
        return root;
    }

    /**
     * 
     * @param root 
     */
    public void setRoot(Byte root) {
        this.root = root;
    }

    /**
     * 
     * @return zone 
     */
    public Integer getZone() {
        return zone;
    }

    /**
     * 
     * @param zone 
     */
    public void setZone(Integer zone) {
        this.zone = zone;
    }

    /**
     * 状态
     * @return status 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态
     * @param status 状态
     */
    public void setStatus(Integer status) {
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