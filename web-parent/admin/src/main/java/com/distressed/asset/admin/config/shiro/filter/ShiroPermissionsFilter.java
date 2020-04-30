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

package com.distressed.asset.admin.config.shiro.filter;

import com.distressed.asset.admin.config.shiro.ShiroService;
import com.distressed.asset.common.utils.SpringUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Shiro验证是否有权限的过滤器。
 *
 * @author hongchao zhao at 2019-12-6 10:22
 */
//@Component
public class ShiroPermissionsFilter extends PermissionsAuthorizationFilter {

    private static Logger LOG = LoggerFactory.getLogger(ShiroPermissionsFilter.class);

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestUrl = httpServletRequest.getRequestURI();
        String permsUrl = "perms[" + requestUrl + "]";
        //LOG.debug("request===>" + requestUrl + ",permsUrl===>" + permsUrl);
        // 记录日志
        Subject subject = this.getSubject(request, response);
        //获取当前系统权限列表
        ShiroService shiroService = SpringUtil.getBean(ShiroService.class);
        Map<String, String> permsMap = shiroService.getCurrentChainDefinitionMap();
        //true需要鉴权; false不需要鉴权
        boolean isPermitted = false;
        if (permsMap != null && permsMap.containsValue(permsUrl)) {
            isPermitted = true;
        }
        if (isPermitted) {
            //true有权限; false没有权限
            return subject.isPermitted(requestUrl);
        } else {
            //说明当前请求无需要鉴权
            return true;
        }
        //LOG.debug("permsMap===>【{}】", permsMap);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        LOG.debug("当前用户无权限，调用无权限返回方法");
        PrintWriter printWriter = null;
        try {
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.setContentType("application/json");
            printWriter = servletResponse.getWriter();
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            printWriter.write("{\"error\": \"没权限，请联系管理员！\"}");
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return false;
    }
}
