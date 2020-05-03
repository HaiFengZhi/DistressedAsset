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

package com.distressed.asset.portalweb.config.shiro;

import com.distressed.asset.common.cache.RedisService;
import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.common.utils.CryptographUtils;
import com.distressed.asset.common.utils.MyDateUtils;
import com.distressed.asset.common.utils.MyStringUtils;
import com.distressed.asset.portal.dto.UserDTO;
import com.distressed.asset.portal.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用于进行权限信息的验证，继承AuthorizingRealm。并且重写父类中的doGetAuthorizationInfo（权限相关）、doGetAuthenticationInfo（身份认证）这两个方法。
 *
 * @author hongchao zhao at 2019-11-21 14:46
 */
public class MyWebShiroRealm extends AuthorizingRealm {

    private static Logger LOG = LoggerFactory.getLogger(MyWebShiroRealm.class);

    @Resource
    private RedisService redisService;
    @Resource
    private UserService userService;

    /**
     * 用户登录次数计数  redisKey 前缀
     */
    private static final String SHIRO_LOGIN_COUNT = "shiro_web_login_count_";

    /**
     * 用户登录是否被锁定一小时 redisKey 前缀
     */
    private static final String SHIRO_IS_LOCK = "shiro_web_is_lock_";

    /**
     * 授权(接口保护，验证接口调用权限时调用)。
     *
     * @param principalCollection 用户对象。
     * @return 授权。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LOG.info("授予角色权限方法：MyWebShiroRealm.doGetAuthorizationInfo()");
        UserDTO adminUser = (UserDTO) principalCollection.getPrimaryPrincipal();
        //LOG.debug("当前登陆用户为【{}】，进行用户授权行为。。。", adminUser.getNickname());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roleSet = new HashSet<String>();
        //后期可以判断用户角色来赋予相应权限
        roleSet.add("user");
        info.setRoles(roleSet);
        //LOG.info("授予用户【{}】角色【{}】成功。", adminUser.getLoginUsername(), roleSet);
        Set<String> stringPermissions = new HashSet<String>();
        stringPermissions.add("");
        info.setStringPermissions(stringPermissions);
        //LOG.info("授予用户【{}】权限【{}】成功。", adminUser.getLoginUsername(), stringPermissions);
        return info;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份。
     *
     * @param authcToken 是用来验证用户身份。
     * @return 身份验证。
     * @throws AuthenticationException 验证异常。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        LOG.info("身份认证方法：MyWebShiroRealm.doGetAuthenticationInfo()");
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String name = token.getUsername();
        String password = String.valueOf(token.getPassword());
        LOG.debug("当前登陆用户【{}】【{}】。", name, password);
        //访问一次，计数一次
        redisService.incr(SHIRO_LOGIN_COUNT + name, 1);
        //计数大于5时，设置用户被锁定一小时【必然不为空】
        if ((Integer) redisService.get(SHIRO_LOGIN_COUNT + name) >= 5) {
            redisService.set(SHIRO_IS_LOCK + name, "LOCK");
            redisService.expire(SHIRO_IS_LOCK + name, 1, TimeUnit.HOURS);
        }
        if ("LOCK".equals(redisService.get(SHIRO_IS_LOCK + name))) {
            throw new DisabledAccountException("由于密码输入错误次数大于5次，帐号已经禁止登录！");
        }

        //校验当前用户是否登陆成功
        ResultBean<UserDTO> resultBean = userService.checkUserLogin(name, CryptographUtils.MD5(password));
        UserDTO user;
        if (!resultBean.isSuccess()) {
            throw new AccountException("帐号或密码不正确！");
        } else {
            user = resultBean.getData();
            if (1!=user.getStatus()) {
                throw new DisabledAccountException("帐号被锁定已经禁止登录！");
            } else if (!MyStringUtils.equalsIgnoreCase(user.getPassword(),CryptographUtils.MD5(password))) {
                throw new DisabledAccountException("登陆密码错误！");
            } else {
                //登陆成功，更新登录时间 last login time
                user.setLastLoginTime(MyDateUtils.now());
                userService.updateUser(user);
                //清空登录计数
                redisService.set(SHIRO_LOGIN_COUNT + name, 0);
            }
        }
        LOG.info("Shiro身份认证成功，登陆用户：【{}】", name);
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    /**
     * 清空当前登陆用户的缓存，在用户访问权限功能时会自动重新获取最新权限。
     */
    public void clearAuthorization(){
        LOG.debug("清空当前登陆用户的缓存权限。。。");
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
