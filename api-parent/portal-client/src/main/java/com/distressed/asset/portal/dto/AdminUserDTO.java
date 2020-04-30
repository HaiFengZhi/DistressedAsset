package com.distressed.asset.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 后台用户信息表。
 *
 * @author zhaohc
 * @date 2020-04-30
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserDTO implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 登陆名。
     */
    private String loginUsername;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 安全密钥
     */
    private String securityPassword;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次登陆时间
     */
    private Date lastLoginTime;

    /**
     * 用户类型
     */
    private Integer type;

    /**
     * 状态:1-启用 0-禁用
     */
    private Boolean status;

    /**
     * 绑定手机
     */
    private String boundCellphone;

    /**
     * 绑定邮箱
     */
    private String boundEmail;

    /**
     * 是否修改密码
     */
    private Integer modifyPwdOr;

    /**
     * admin_admin_user
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 登陆名。
     * @return login_username 登陆名。
     */
    public String getLoginUsername() {
        return loginUsername;
    }

    /**
     * 登陆名。
     * @param loginUsername 登陆名。
     */
    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername == null ? null : loginUsername.trim();
    }

    /**
     * 昵称
     * @return nickname 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 昵称
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * 登陆密码
     * @return password 登陆密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 登陆密码
     * @param password 登陆密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 安全密钥
     * @return security_password 安全密钥
     */
    public String getSecurityPassword() {
        return securityPassword;
    }

    /**
     * 安全密钥
     * @param securityPassword 安全密钥
     */
    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword == null ? null : securityPassword.trim();
    }

    /**
     * 头像
     * @return portrait 头像
     */
    public String getPortrait() {
        return portrait;
    }

    /**
     * 头像
     * @param portrait 头像
     */
    public void setPortrait(String portrait) {
        this.portrait = portrait == null ? null : portrait.trim();
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
     * 上次登陆时间
     * @return last_login_time 上次登陆时间
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 上次登陆时间
     * @param lastLoginTime 上次登陆时间
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 用户类型
     * @return type 用户类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 用户类型
     * @param type 用户类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 状态:1-启用 0-禁用
     * @return status 状态:1-启用 0-禁用
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 状态:1-启用 0-禁用
     * @param status 状态:1-启用 0-禁用
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 绑定手机
     * @return bound_cellphone 绑定手机
     */
    public String getBoundCellphone() {
        return boundCellphone;
    }

    /**
     * 绑定手机
     * @param boundCellphone 绑定手机
     */
    public void setBoundCellphone(String boundCellphone) {
        this.boundCellphone = boundCellphone == null ? null : boundCellphone.trim();
    }

    /**
     * 绑定邮箱
     * @return bound_email 绑定邮箱
     */
    public String getBoundEmail() {
        return boundEmail;
    }

    /**
     * 绑定邮箱
     * @param boundEmail 绑定邮箱
     */
    public void setBoundEmail(String boundEmail) {
        this.boundEmail = boundEmail == null ? null : boundEmail.trim();
    }

    /**
     * 是否修改密码
     * @return modify_pwd_or 是否修改密码
     */
    public Integer getModifyPwdOr() {
        return modifyPwdOr;
    }

    /**
     * 是否修改密码
     * @param modifyPwdOr 是否修改密码
     */
    public void setModifyPwdOr(Integer modifyPwdOr) {
        this.modifyPwdOr = modifyPwdOr;
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