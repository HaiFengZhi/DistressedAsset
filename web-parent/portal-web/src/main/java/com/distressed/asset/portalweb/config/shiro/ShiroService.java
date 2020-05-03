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
import com.distressed.asset.common.utils.CommonUtils;
import com.distressed.asset.portal.dto.AdminUserDTO;
import com.distressed.asset.portal.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Shiro工具类。
 *
 * @author hongchao zhao at 2019-12-9 16:09
 */
@Service
public class ShiroService {
    private static Logger LOG = LoggerFactory.getLogger(ShiroService.class);

    @Resource
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    @Resource
    private MenuService menuService;
    @Resource
    private RedisSessionDAO redisSessionDAO;
    @Resource
    private RedisService redisService;

    /**
     * 初始化当前系统所有权限列表。
     *
     * @return 权限列表。
     */
    private Map<String, String> loadFilterChainDefinitions() {
        //获取初始化鉴权列表
        Map<String, String> filterChainDefinitionMap = ShiroUtils.generateAnonDefinitionMap();
        //获取系统实时权限列表
        /*ResultBean<List<BaseResourceDTO>> resourceList = menuService.getMenuList();
        if (resourceList.isSuccess() && resourceList.getData() != null) {
            for (BaseResourceDTO resource : resourceList.getData()) {
                //去掉空权限和已被禁用的权限
                if (MyStringUtils.isNotBlank(resource.getAccessUrl()) && resource.getShowOr()) {
                    String permission = "perms[" + resource.getAccessUrl() + "]";
                    filterChainDefinitionMap.put(resource.getAccessUrl(), permission);
                }
            }
        }*/
        //filterChainDefinitionMap.put("/add", "perms[权限添加]");
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        // <!-- user 指的是用户认证通过或者配置了Remember Me记住用户登录状态后可访问-->
        // <!-- kickout 为自定义的单点登陆踢人过滤器-->
        // <!-- perms 为自定义的鉴权过滤器-->
//        filterChainDefinitionMap.put("/**", "authc,kickout,perms");
        filterChainDefinitionMap.put("/**", "anon");
        //LOG.debug("初始化系统所有权限列表【{}】。。。",filterChainDefinitionMap);
        return filterChainDefinitionMap;
    }

    /**
     * 重新加载权限
     */
    public void updatePermission() {
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean
                        .getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim()
                        .replace(" ", "");
                manager.createChain(url, chainDefinition);
            }
            LOG.debug("更新权限成功！！");
        }
    }

    /**
     * 获取当前系统权限过滤列表。
     *
     * @return 权限过滤列表。
     */
    public Map<String, String> getCurrentChainDefinitionMap() {
        if (shiroFilterFactoryBean != null) {
            return shiroFilterFactoryBean.getFilterChainDefinitionMap();
        }
        return null;
    }

    /**
     * 更新处于登陆状态的用户信息，用于修改管理员信息时，不用重新登陆。
     *
     * @param adminUser 管理员信息。
     */
    public void updateAdminSession(AdminUserDTO adminUser){
        Session session = getSessionByUserId(adminUser.getId());
        if (session == null) {
            return;
        }

        Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (attribute == null) {
            return;
        }

        AdminUserDTO user = (AdminUserDTO) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
        if (!adminUser.getId().equals(user.getId())) {
            return;
        }

        //更新登陆管理员信息【】
        user.setLoginUsername(adminUser.getLoginUsername());
        user.setNickname(adminUser.getNickname());
        user.setBoundEmail(adminUser.getBoundEmail());
        user.setBoundCellphone(adminUser.getBoundCellphone());
        user.setPortrait(adminUser.getPortrait());
        user.setStatus(adminUser.getStatus());
        redisSessionDAO.update(session);
        //把当前更新的用户信息丢在Redis中，减少去数据库交互频率
        redisService.set(ShiroUtils.ONLINE_UPDATE_SESSION_USER_INFO + adminUser.getId(), user);
        LOG.debug("更新管理员【{}】缓存信息。。。", adminUser.getLoginUsername());
    }

    /**
     * 获取当前系统在线管理员列表。
     *
     * @return 管理员列表。
     */
    public List<AdminUserDTO> showOnlineUserList(){
        List<AdminUserDTO> list = new ArrayList<>();
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        AdminUserDTO user;
        Object attribute;
        for (Session session : sessions) {
            attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            user = (AdminUserDTO) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (user == null) {
                continue;
            }
            //过滤掉已经踢出的用户
            Object online = session.getAttribute(ShiroUtils.ONLINE_KICKED_OUT_USER);
            if (online != null && (Boolean) online) {
                continue;
            }
            list.add(user);
        }
        return list;
    }

    /**
     * 获取指定用户名【用户昵称】的Session。
     *
     * @param userId 用户编号。
     * @return 当前用户绑定的Session。
     */
    private Session getSessionByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        AdminUserDTO user;
        Object attribute;
        for (Session session : sessions) {
            attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (attribute == null) {
                continue;
            }
            user = (AdminUserDTO) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
            if (user == null) {
                continue;
            }
            if (Objects.equals(user.getId(), userId)) {
                LOG.debug("根据用户编号【{}】获取到该用户当前的登陆Session！", userId);
                return session;
            }
        }
        return null;
    }

    /**
     * 删除用户缓存信息。
     *
     * @param userId        用户编号。
     * @param isRemoveSession 是否删除session，删除后用户需重新登录
     */
    public void kickOutUser(Long userId, boolean isRemoveSession) {
        Session session = getSessionByUserId(userId);
        if (session == null) {
            return;
        }

        Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (attribute == null) {
            return;
        }

        AdminUserDTO user = (AdminUserDTO) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
        if (!userId.equals(user.getId())) {
            return;
        }

        //删除session
        if (isRemoveSession) {
            LOG.debug("标记用户【{}】的登陆Session为退出登陆状态，强制退出登陆状态！", userId);
            //避免直接删除Session后，与记住密码功能相冲突，这里添加特殊标识到在登陆鉴权里去执行退出操作
            //redisSessionDAO.delete(session);
            session.setAttribute(ShiroUtils.ONLINE_KICKED_OUT_USER, true);
            redisSessionDAO.update(session);
        }
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        Authenticator authc = securityManager.getAuthenticator();
        //删除cache，在访问受限接口时会重新授权
        ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
        LOG.debug("删除用户【{}】缓存，在访问受限接口时会重新授权！", userId);
    }

    /**
     * 根据角色编号，清除当前角色下的所有用户的缓存权限。
     *
     * @param roleId 角色编号。
     */
    public void cleanCachedAuthorByRoleId(Long roleId){
        ResultBean<String> adminIdsBean = menuService.selectAdminIdsByRoleId(roleId);
        if (adminIdsBean.isSuccess()&&adminIdsBean.getData()!=null){
            String[] adminIds = adminIdsBean.getData().split(",");
            for (String adminId : adminIds){
                //循环清除当前登陆用户的缓存权限，并不强制退出用户登陆状态
                kickOutUser(CommonUtils.getLong(adminId), false);
            }
        }else {
            LOG.debug("当前角色未分配用户。。。");
        }
    }

    /**
     * 根据权限编号，清除当前权限下所有角色对应的所有用户的缓存权限。
     *
     * @param resourceId 权限编号。
     */
    public void cleanCachedAuthorByResourceId(Long resourceId){
        ResultBean<String> roleIdsBean = menuService.selectRoleIdsByResourceId(resourceId);
        if (roleIdsBean.isSuccess()&&roleIdsBean.getData()!=null){
            String[] roleIds = roleIdsBean.getData().split(",");
            for (String roleId : roleIds){
                cleanCachedAuthorByRoleId(CommonUtils.getLong(roleId));
            }
        }else {
            LOG.debug("当前权限未分配角色。。。");
        }

    }

}
