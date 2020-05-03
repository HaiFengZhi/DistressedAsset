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

import com.distressed.asset.portal.dto.UserDTO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro框架工具类。
 *
 * @author hongchao zhao at 2019-12-10 11:59
 */
public class ShiroUtils {

    /**
     * 在Session中添加踢出登陆的标识。
     */
    public static final String ONLINE_KICKED_OUT_USER = "WEB_ONLINE_KICKED_OUT_USER";
    /**
     * 更新登陆用户信息
     */
    public static final String ONLINE_UPDATE_USER = "WEB_ONLINE_UPDATE_USER";
    public static final String ONLINE_UPDATE_SESSION_USER_INFO = "WEB_ONLINE_UPDATE_SESSION_USER_INFO_";

    /**
     * 初始化公用的无需鉴权的权限列表。
     *
     * @return 鉴权列表。
     */
    public static Map<String, String> generateAnonDefinitionMap(){
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
//        filterChainDefinitionMap.put("/static/css/**", "anon");
//        filterChainDefinitionMap.put("/static/images/**", "anon");
//        filterChainDefinitionMap.put("/static/layui/**", "anon");
//        filterChainDefinitionMap.put("/static/layui/css/**", "anon");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，会自动清除相关缓存
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/login", "anon");

        // 查看SQL监控（druid）
        filterChainDefinitionMap.put("/druid/**", "anon");
        //验证码相关
        filterChainDefinitionMap.put("/checkCode", "anon");
        filterChainDefinitionMap.put("/code", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        //登陆在线人数限制
        filterChainDefinitionMap.put("/kickout", "anon");
        filterChainDefinitionMap.put("/403", "anon");
        return filterChainDefinitionMap;
    }

    /**
     * 清空当前登陆用户的缓存权限。
     */
    public static void clearCachedAuthorization(){
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MyWebShiroRealm realm = (MyWebShiroRealm)rsm.getRealms().iterator().next();
        realm.clearAuthorization();
    }

    /**
     * 切换身份，登录后，动态更改当前登陆中的subject的用户属性。
     *
     * @param userInfo 登陆管理员信息。
     */
    public static void updateLoginUser(UserDTO userInfo) {
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(userInfo, realmName);
        subject.runAs(newPrincipalCollection);
    }

    /**
     * 获取当前登陆用户信息。
     *
     * @return 用户信息。
     */
    public static UserDTO getLoginUser() {
        return (UserDTO) SecurityUtils.getSubject().getPrincipal();
    }

    public static void main(String[] args) {
        //【{/static/**=anon, /logout=logout, /login=anon, /druid/**=anon, /checkCode=anon, /code=anon, /user/login=anon, /favicon.ico=anon, /kickout=anon,
        // /403=anon, /test=perms[/test], /platform/list=perms[/platform/list], /platform/roleList=perms[/platform/roleList], /platform/adminList=perms[/platform/adminList], /platform/generatePwd=perms[/platform/generatePwd], /admin/showInfo=perms[/admin/showInfo], /admin/toPortrait=perms[/admin/toPortrait], /admin/toPassword=perms[/admin/toPassword], /user/list=perms[/user/list], /**=user,kickout,perms}】
        Map<String, String> filterChainDefinitionMap = generateAnonDefinitionMap();
        filterChainDefinitionMap.put("/test","perms[/test]");
        filterChainDefinitionMap.put("/platform/list","perms[/platform/list]");
        if (filterChainDefinitionMap.containsValue("perms[/platform/list]")){
            System.out.println("拥有当前权限限制");
        }else {
            System.out.println("木有当前权限限制");
        }
    }
}
