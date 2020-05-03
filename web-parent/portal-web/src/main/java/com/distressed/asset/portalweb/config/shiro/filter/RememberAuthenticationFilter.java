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

package com.distressed.asset.portalweb.config.shiro.filter;

import com.distressed.asset.portal.dto.UserDTO;
import com.distressed.asset.common.cache.RedisService;
import com.distressed.asset.portalweb.config.shiro.ShiroUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 重写Shiro记住我功能，在被踢掉后要做退出操作。
 *
 * @author hongchao zhao at 2019-12-12 16:09
 */
public class RememberAuthenticationFilter extends FormAuthenticationFilter {

    private static Logger LOG = LoggerFactory.getLogger(RememberAuthenticationFilter.class);

    @Resource
    private RedisService redisService;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestUrl = httpServletRequest.getRequestURI();
        //LOG.debug("【{}】进行登陆鉴权，此处会将记住密码的用户放行。。。", requestUrl);
        //如果 isAuthenticated 为 false 证明不是登录过的，同时 isRememberd 为true 证明是没登陆直接通过记住我功能进来的
        //将标注为踢出登陆状态的用户手动退出登陆
        Session session = subject.getSession();
        Object attribute = session.getAttribute(ShiroUtils.ONLINE_KICKED_OUT_USER);
        //LOG.debug("session【{}】attribute==【{}】", session.getId(), attribute);
        if (attribute != null && (Boolean) attribute) {
            LOG.debug("把当前用户踢出登陆状态。。。");
            subject.logout();
        }
        //判断是否需要刷新管理员缓存信息
        UserDTO loginUser = (UserDTO) subject.getPrincipal();
        if (loginUser != null) {
            Object cacheInfo = redisService.get(ShiroUtils.ONLINE_UPDATE_SESSION_USER_INFO + loginUser.getId());
            if (cacheInfo != null) {
                UserDTO cacheUser = (UserDTO) cacheInfo;
                LOG.debug("更新当前登陆用户【{}】信息。。。", cacheUser.getLoginUsername());
                ShiroUtils.updateLoginUser(cacheUser);
                //清除无用缓存
                redisService.del(ShiroUtils.ONLINE_UPDATE_SESSION_USER_INFO + loginUser.getId());
            }
        }
        return subject.isAuthenticated() || subject.isRemembered();
    }
}
