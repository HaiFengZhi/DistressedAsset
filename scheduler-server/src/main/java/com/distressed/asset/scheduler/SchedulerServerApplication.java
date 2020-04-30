package com.distressed.asset.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//启用feign客户端，可配置多个
@EnableFeignClients(basePackages ={"com.distressed.asset"})
public class SchedulerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerServerApplication.class, args);
    }

}
