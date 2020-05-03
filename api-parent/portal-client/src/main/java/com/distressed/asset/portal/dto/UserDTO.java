package com.distressed.asset.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表。
 *
 * @author yanjie wan at 2019-10-29 11:50
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO implements Serializable {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
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
     * 用户头像
     */
    private String portrait;

    /**
     * 绑定手机号码
     */
    private String boundCellphone;

    /**
     * 绑定邮箱
     */
    private String boundEmail;

    /**
     * 绑定微信唯一openId
     */
    private String wechatKey;

    /**
     * 个人邀请码
     */
    private String shareCode;

    /**
     * 注册时间
     */
    private Date registeredTime;

    /**
     * 最后登陆时间
     */
    private Date lastLoginTime;

    /**
     * 用户类型【0：普通会员，1：VIP会员】
     */
    private Integer type;

    /**
     * VIP开始时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vipStartDate;

    /**
     * VIP结束时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vipEndDate;

    /**
     * VIP状态【1：有效，0：失效】
     */
    private Integer vipStatus;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 0个人用户,1企业用户
     */
    private Integer userType;

    /**
     * 推荐人邀请码
     */
    private String recommendCode;

    /**
     * 状态
     */
    private Integer status;

    /**
     * os_user
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     * @return id 用户id
     */
    public Long getId() {
        return id;
    }

    /**
     * 用户id
     * @param id 用户id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 用户名
     * @return login_username 用户名
     */
    public String getLoginUsername() {
        return loginUsername;
    }

    /**
     * 用户名
     * @param loginUsername 用户名
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
     * 用户头像
     * @return portrait 用户头像
     */
    public String getPortrait() {
        return portrait;
    }

    /**
     * 用户头像
     * @param portrait 用户头像
     */
    public void setPortrait(String portrait) {
        this.portrait = portrait == null ? null : portrait.trim();
    }

    /**
     * 绑定手机号码
     * @return bound_cellphone 绑定手机号码
     */
    public String getBoundCellphone() {
        return boundCellphone;
    }

    /**
     * 绑定手机号码
     * @param boundCellphone 绑定手机号码
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
     * 绑定微信唯一openId
     * @return wechat_key 绑定微信唯一openId
     */
    public String getWechatKey() {
        return wechatKey;
    }

    /**
     * 绑定微信唯一openId
     * @param wechatKey 绑定微信唯一openId
     */
    public void setWechatKey(String wechatKey) {
        this.wechatKey = wechatKey == null ? null : wechatKey.trim();
    }

    /**
     * 个人邀请码
     * @return share_code 个人邀请码
     */
    public String getShareCode() {
        return shareCode;
    }

    /**
     * 个人邀请码
     * @param shareCode 个人邀请码
     */
    public void setShareCode(String shareCode) {
        this.shareCode = shareCode == null ? null : shareCode.trim();
    }

    /**
     * 注册时间
     * @return registered_time 注册时间
     */
    public Date getRegisteredTime() {
        return registeredTime;
    }

    /**
     * 注册时间
     * @param registeredTime 注册时间
     */
    public void setRegisteredTime(Date registeredTime) {
        this.registeredTime = registeredTime;
    }

    /**
     * 最后登陆时间
     * @return last_login_time 最后登陆时间
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 最后登陆时间
     * @param lastLoginTime 最后登陆时间
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 用户类型【0：普通会员，1：VIP会员】
     * @return type 用户类型【0：普通会员，1：VIP会员】
     */
    public Integer getType() {
        return type;
    }

    /**
     * 用户类型【0：普通会员，1：VIP会员】
     * @param type 用户类型【0：普通会员，1：VIP会员】
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * VIP开始时间
     * @return vip_start_date VIP开始时间
     */
    public Date getVipStartDate() {
        return vipStartDate;
    }

    /**
     * VIP开始时间
     * @param vipStartDate VIP开始时间
     */
    public void setVipStartDate(Date vipStartDate) {
        this.vipStartDate = vipStartDate;
    }

    /**
     * VIP结束时间
     * @return vip_end_date VIP结束时间
     */
    public Date getVipEndDate() {
        return vipEndDate;
    }

    /**
     * VIP结束时间
     * @param vipEndDate VIP结束时间
     */
    public void setVipEndDate(Date vipEndDate) {
        this.vipEndDate = vipEndDate;
    }

    /**
     * VIP状态【1：有效，0：失效】
     * @return vip_status VIP状态【1：有效，0：失效】
     */
    public Integer getVipStatus() {
        return vipStatus;
    }

    /**
     * VIP状态【1：有效，0：失效】
     * @param vipStatus VIP状态【1：有效，0：失效】
     */
    public void setVipStatus(Integer vipStatus) {
        this.vipStatus = vipStatus;
    }

    /**
     * 个性签名
     * @return signature 个性签名
     */
    public String getSignature() {
        return signature;
    }

    /**
     * 个性签名
     * @param signature 个性签名
     */
    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    /**
     * 0个人用户,1企业用户
     * @return user_type 0个人用户,1企业用户
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * 0个人用户,1企业用户
     * @param userType 0个人用户,1企业用户
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    /**
     * 推荐人邀请码
     * @return recommend_code 推荐人邀请码
     */
    public String getRecommendCode() {
        return recommendCode;
    }

    /**
     * 推荐人邀请码
     * @param recommendCode 推荐人邀请码
     */
    public void setRecommendCode(String recommendCode) {
        this.recommendCode = recommendCode == null ? null : recommendCode.trim();
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