package com.distressed.asset.portalweb.controller;

import com.distressed.asset.common.cache.RedisService;
import com.distressed.asset.portal.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 测试核心控制类。
 *
 * @author hongchao zhao at 2019-10-14 16:14
 */
@Controller
public class IndexController {

    private static Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private DemoService demoService;
    @Resource
    private RedisService redisService;

    //商城首页入口
    @RequestMapping(value = {"/index", "/"})
    public String index(HttpServletRequest request, Model model) {
        LOG.debug("首页入口。。。");
        return "index";
    }
}
