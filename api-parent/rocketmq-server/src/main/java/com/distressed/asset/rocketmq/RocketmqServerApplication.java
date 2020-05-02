package com.distressed.asset.rocketmq;

import com.alibaba.fastjson.JSON;
import com.distressed.asset.rocketmq.client.RocketMQService;
import com.distressed.asset.rocketmq.message.MessageConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@EnableFeignClients(basePackages ={"com.distressed.asset"})
@RestController
public class RocketmqServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqServerApplication.class, args);
    }

    @Resource
    private RocketMQService rocketMQService;

    @RequestMapping("/test")
    @ResponseBody
    public String welcome(HttpServletRequest request, Model model) {
        String msg = "我是终结者T";
        for (int i = 1; i <= 100; i++) {
            rocketMQService.sendOrderMsg(JSON.toJSONString(msg + i), MessageConstants.Topic.TOPIC_DEMO_TEST, MessageConstants.Tag.TAG_SAY_HELLO, "xxx");
        }
        return msg;
    }
}
