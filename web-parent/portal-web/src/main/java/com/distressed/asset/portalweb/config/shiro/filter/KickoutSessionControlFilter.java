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

import com.alibaba.fastjson.JSON;
import com.distressed.asset.portal.dto.AdminUserDTO;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Shiro过滤器：限制当前用户只能同时在线一个。
 * 1.读取当前登录用户名，获取在缓存中的sessionId队列
 * 2.判断队列的长度，大于最大登录限制的时候，按踢出规则
 * 将之前的sessionId中的session域中存入kickout：true，并更新队列缓存
 * 3.判断当前登录的session域中的kickout如果为true，
 * 想将其做退出登录处理，然后再重定向到踢出登录提示页面
 *
 * @author hongchao zhao at 2019-12-9 11:42
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

    private static Logger LOG = LoggerFactory.getLogger(KickoutSessionControlFilter.class);

    //踢出后到的地址
    private String kickoutUrl;
    //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private boolean kickoutAfter = false;
    //同一个帐号最大会话数 默认1
    private int maxSession = 1;

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_web_redis_cache");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        AdminUserDTO user = (AdminUserDTO) subject.getPrincipal();
        //用登陆名，保证唯一，昵称可能会重复
        String username = user.getLoginUsername();
        Serializable sessionId = session.getId();
        //LOG.debug("当前用户【{}】sessionId===【{}】，当前时间【{}】", username, sessionId, MyDateUtils.formatByClassic(MyDateUtils.now()));

        //读取缓存   没有就存入
        Deque<Serializable> deque = cache.get(username);
        //如果此用户没有session队列，也就是还没有登录过，缓存中没有
        //就new一个空队列，不然deque对象为空，会报空指针
        if (deque == null) {
            deque = new LinkedList<Serializable>();
        }
        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            //将sessionId存入队列
            deque.push(sessionId);
            //将用户的sessionId队列缓存
            cache.put(username, deque);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            //控制踢先登陆的还是后登陆的
            if (kickoutAfter) {
                //如果踢出后者
                kickoutSessionId = deque.removeFirst();
                //踢出后再更新下缓存队列
                cache.put(username, deque);
            } else {
                //否则踢出前者
                kickoutSessionId = deque.removeLast();
                //踢出后再更新下缓存队列
                cache.put(username, deque);
            }


            try {
                //获取被踢出的sessionId的session对象
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {//ignore exception
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null && (Boolean) session.getAttribute("kickout")) {
            //会话被踢出了
            try {
                //退出登录
                LOG.debug("用户【{}】被踢出登陆状态。。。", username);
                subject.logout();
            } catch (Exception e) { //ignore
            }
            saveRequest(request);

            Map<String, String> resultMap = new HashMap<String, String>();
            //判断是不是Ajax请求
            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
                resultMap.put("user_status", "300");
                resultMap.put("message", "您的账号在另一台设备上登录,如非本人操作，请立即修改密码！");
                //输出json串
                out(response, resultMap);
            } else {
                //重定向
                WebUtils.issueRedirect(request, response, kickoutUrl);
            }
            return false;
        }

        return true;
    }

    /**
     * 辅助方法：输出Json字符串。
     *
     * @param response  响应对象。
     * @param resultMap Map对象。
     * @throws IOException IO异常。
     */
    private void out(ServletResponse response, Map<String, String> resultMap) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSONString(resultMap));
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println("KickoutSessionFilter.class 输出JSON异常，可以忽略。");
        }
    }
}
