package com.distressed.asset.portal;

import com.distressed.asset.common.utils.MyStringUtils;
import com.distressed.asset.portal.service.DemoService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@SpringBootApplication
@EnableFeignClients(basePackages ={"com.distressed.asset"})
//配置扫描mapper接口的地址，不用在接口类添加@Mapper
@MapperScan(basePackages = {"com.distressed.asset.portal.dao"})
//开启缓存机制
@EnableCaching
//启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@EnableTransactionManagement
@RestController
public class PortalServerApplication {

    private static Logger LOG = LoggerFactory.getLogger(PortalServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PortalServerApplication.class, args);
    }

    @Resource
    private DemoService demoService;

    @RequestMapping("/test")
    @ResponseBody
    public String welcome(HttpServletRequest request, Model model, @RequestParam(required = false) String name, @RequestParam(required = false) String code) {
        String showDesc = "";
        if (MyStringUtils.isNoneBlank(name)){
            String msg = demoService.sayHi(name);
            LOG.debug("msg===>" + msg);
            showDesc = msg.concat("\r\n");
        }
        if (MyStringUtils.isNoneBlank(code)){
            String area = demoService.getArea(code);
            LOG.debug("area===>" + area);
            showDesc += area;
        }

        return showDesc;
    }

}
