package com.distressed.asset.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages ={"com.distressed.asset"})
//开启缓存机制
@EnableCaching
public class FileServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

}
