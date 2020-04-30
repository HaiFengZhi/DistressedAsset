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

package com.distressed.asset.admin.controller;

import com.distressed.asset.admin.config.shiro.ShiroService;
import com.distressed.asset.portal.dto.AdminUserDTO;
import com.distressed.asset.portal.service.MenuService;
import com.distressed.asset.common.cache.RedisService;
import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.common.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 后台核心管理类。
 *
 * @author hongchao zhao at 2020-1-13 10:28
 */
@Controller
public class IndexController {
    private static Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private RedisService redisService;
    @Resource
    private ShiroService shiroService;
    @Resource
    private MenuService menuService;

    @Value("${distressed.asset.file.manager.show}")
    private String filePath;

    /**
     * 生成图形验证码。
     *
     * @param randomPageId 页面标识。
     * @return {@link ResponseEntity}。
     */
    @RequestMapping("/code")
    public ResponseEntity<byte[]> generateImageCode(@RequestParam(value = "randomPageId") String randomPageId) {
        String randomCode = VerifyCodeUtils.random(4);
        ResponseEntity<byte[]> responseEntity = GraphicsUtils.generateImageCodeForSpringMVC(randomCode);
        //在redis 中保存键值【randomPageId=GUIDUtils.makeUUID();】
        redisService.set(Constants.Global.PARAM_VERIFICATION_CODE + randomPageId, randomCode, 180);
        return responseEntity;
    }

    /**
     * 校验码验证。
     *
     * @param randomPageId 页面随机码。
     * @param checkCode    验证码、
     * @return 0：；1：；2：。
     */
    @RequestMapping(
            value = "/checkCode",
            produces = {"application/json"})
    @ResponseBody
    public int checkImageCode(String randomPageId, String checkCode) {
        if (StringUtils.isNotEmpty(randomPageId)) {
            Object cacheCode = redisService.get(Constants.Global.PARAM_VERIFICATION_CODE + randomPageId);
            if (cacheCode != null) {
                // 1:正确,0:错误。
                return cacheCode.toString().equalsIgnoreCase(checkCode) ? 1 : 0;
            } else {
                //验证码失效。
                return 2;
            }
        }
        return 1;
    }

    /**
     * 进入登陆页面。
     *
     * @param request 请求。
     * @param model   页面元素。
     * @return 登陆页面。
     */
    @RequestMapping(value = {"/login", "/index", "/"})
    public String login(HttpServletRequest request, Model model) {
        LOG.debug("进入登陆页面。。。");
        model.addAttribute("randomPageId", GUIDUtils.makeUUID());
        return "login";
    }

    /**
     * 登陆校验。
     *
     * @param username     用户名。
     * @param password     密码。
     * @param code         图形验证码。
     * @param randomPageId 页面唯一标识。
     * @param rememberMe   是否记住密码。
     * @return 登陆结果。
     */
    @ResponseBody
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResultBean doLogin(String username, String password, String code, String randomPageId, Boolean rememberMe) {
        LOG.debug("当前用户登陆【{}】【{}】【{}】【{}】【{}】。。。", username, password, code, randomPageId, rememberMe);
        if (CommonUtils.isBlank(username, password, code)) {
            return ResultBean.failed(0, "用户名或密码以及验证码不能为空！");
        }
        //校验图形验证码，防止越过前台JS校验
        Object cacheCode = redisService.get(Constants.Global.PARAM_VERIFICATION_CODE + randomPageId);
        if (cacheCode != null) {
            if (!cacheCode.toString().equalsIgnoreCase(code)) {
                return ResultBean.failed(0, "图形验证码输入错误，请重新输入！");
            }
        } else {
            return ResultBean.failed(0, "图形验证码已失效，请重新获取！");
        }

        try {
            LOG.debug("调用Shiro鉴权系统。。。");
            Subject subject = SecurityUtils.getSubject();

            //封闭当前登陆对象
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe != null);
            //调用Shiro鉴权框架
            subject.login(token);
            LOG.debug("Shiro鉴权登陆完成。。。");
            if (subject.isAuthenticated()) {
                return ResultBean.success("登陆成功！");
            }
        } catch (AccountException e) {
            LOG.error("Shiro鉴权失败：" + e.getMessage());
            return ResultBean.failed(0, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Shiro鉴权失败：" + e.getMessage());
            return ResultBean.failed(0, "登陆鉴权失败，请重新尝试！");
        }
        return ResultBean.failed("登陆失败！");
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean logout() {
        try {
            //退出
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return ResultBean.success();
    }

    //被踢出后跳转的页面
    @RequestMapping(value = "kickout")
    public String kickout() {
        return "kickout";
    }

    //无权限访问跳转的页面
    @RequestMapping(value = "403")
    public String unauthorizedUrl() {
        return "403";
    }

    //模板页面
    @RequestMapping("/welcome")
    public String welcome(HttpServletRequest request, Model model) {
        LOG.debug("进入管理中心欢迎页面。。。");
        //登陆成功后，手动刷新一下系统权限列表，因为初始化时为了快速加载只加载了部分权限
        shiroService.updatePermission();
        //设置会员头像和昵称
        AdminUserDTO adminUser = (AdminUserDTO) SecurityUtils.getSubject().getPrincipal();
        Object portrait = redisService.get(Constants.Global.REDIS_ADMIN_USER_PORTRAIT + adminUser.getId());
        if (portrait == null) {
            if (MyStringUtils.isBlank(adminUser.getPortrait())) {
                //默认贤心头像
                portrait = "http://t.cn/RCzsdCq";
            } else {
                portrait = filePath + adminUser.getPortrait();
            }
            redisService.set(Constants.Global.REDIS_ADMIN_USER_PORTRAIT + adminUser.getId(), portrait);
        }
        model.addAttribute("nickname", adminUser.getNickname());
        model.addAttribute("portrait", portrait);
        return "welcome";
    }

}
