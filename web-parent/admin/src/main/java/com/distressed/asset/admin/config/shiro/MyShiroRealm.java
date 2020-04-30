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

package com.distressed.asset.admin.config.shiro;

import com.distressed.asset.common.cache.RedisService;
import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.common.utils.CryptographUtils;
import com.distressed.asset.common.utils.MyDateUtils;
import com.distressed.asset.common.utils.MyStringUtils;
import com.distressed.asset.portal.dto.AdminUserDTO;
import com.distressed.asset.portal.dto.BaseResourceDTO;
import com.distressed.asset.portal.dto.RoleDTO;
import com.distressed.asset.portal.service.AdminUserService;
import com.distressed.asset.portal.service.MenuService;
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
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用于进行权限信息的验证，继承AuthorizingRealm。并且重写父类中的doGetAuthorizationInfo（权限相关）、doGetAuthenticationInfo（身份认证）这两个方法。
 *
 * @author hongchao zhao at 2019-11-21 14:46
 */
public class MyShiroRealm extends AuthorizingRealm {

    private static Logger LOG = LoggerFactory.getLogger(MyShiroRealm.class);

    @Resource
    private RedisService redisService;
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private MenuService menuService;

    /**
     * 用户登录次数计数  redisKey 前缀
     */
    private static final String SHIRO_LOGIN_COUNT = "shiro_login_count_";

    /**
     * 用户登录是否被锁定一小时 redisKey 前缀
     */
    private static final String SHIRO_IS_LOCK = "shiro_is_lock_";

    /**
     * 授权(接口保护，验证接口调用权限时调用)。
     *
     * @param principalCollection 用户对象。
     * @return 授权。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LOG.info("授予角色权限方法：MyShiroRealm.doGetAuthorizationInfo()");
        AdminUserDTO adminUser = (AdminUserDTO) principalCollection.getPrimaryPrincipal();
        //LOG.debug("当前登陆用户为【{}】，进行用户授权行为。。。", adminUser.getNickname());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取用户角色列表，为当前登陆用户授予角色
        ResultBean<List<RoleDTO>> roleList = menuService.selectRolesByUserId(adminUser.getId());
        Set<String> roleSet = new HashSet<String>();
        if (roleList.isSuccess() && roleList.getData() != null) {
            for (RoleDTO role : roleList.getData()) {
                //去掉空角色和已被禁用的角色
                if (MyStringUtils.isNotBlank(role.getName()) && role.getStatus()) {
                    roleSet.add(role.getName());
                }
            }
        }
        info.setRoles(roleSet);
        //LOG.info("授予用户【{}】角色【{}】成功。", adminUser.getLoginUsername(), roleSet);
        //获取用户权限列表，为当前登陆用户授予权限
        ResultBean<List<BaseResourceDTO>> resourceList = menuService.selectResourcesByUserId(adminUser.getId());
        Set<String> stringPermissions = new HashSet<String>();
        if (resourceList.isSuccess() && resourceList.getData() != null) {
            for (BaseResourceDTO resource : resourceList.getData()) {
                //去掉空权限和已被禁用的权限
                if (MyStringUtils.isNotBlank(resource.getAccessUrl()) && resource.getShowOr()) {
                    stringPermissions.add(resource.getAccessUrl());
                }
            }
        }
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
        LOG.info("身份认证方法：MyShiroRealm.doGetAuthenticationInfo()");
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
        ResultBean<AdminUserDTO> resultBean = adminUserService.checkAdminLogin(name, CryptographUtils.MD5(password));
        AdminUserDTO adminUser;
        if (!resultBean.isSuccess()) {
            throw new AccountException("帐号或密码不正确！");
        } else {
            adminUser = resultBean.getData();
            if (!adminUser.getStatus()) {
                throw new DisabledAccountException("帐号被锁定已经禁止登录！");
            } else {
                //登陆成功，更新登录时间 last login time
                adminUser.setLastLoginTime(MyDateUtils.now());
                adminUserService.updateAdminLogin(adminUser);
                //清空登录计数
                redisService.set(SHIRO_LOGIN_COUNT + name, 0);
            }
        }
        LOG.info("Shiro身份认证成功，登陆用户：【{}】", name);
        return new SimpleAuthenticationInfo(adminUser, password, getName());
    }

    /**
     * 清空当前登陆用户的缓存，在用户访问权限功能时会自动重新获取最新权限。
     */
    public void clearAuthorization(){
        LOG.debug("清空当前登陆用户的缓存权限。。。");
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
