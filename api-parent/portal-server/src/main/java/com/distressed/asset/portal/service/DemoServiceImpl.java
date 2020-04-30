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

package com.distressed.asset.portal.service;

import com.distressed.asset.common.cache.RedisService;
import com.distressed.asset.portal.dao.AreaDAO;
import com.distressed.asset.portal.mapping.Area;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;

/**
 * 测试接口实现类。
 *
 * @author hongchao zhao at 2020-4-30 15:30
 */
@RestController
@CacheConfig(cacheNames = "demoCache")
public class DemoServiceImpl implements DemoService{

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Value("${server.port}")
    String port;

    @Resource
    private RedisService redisService;
    @Resource
    private AreaDAO areaDAO;

    @Override
    public String sayHi(String name) {
        logger.debug("调用当前接口" + port);
//        try {
//            int sleepTime = new Random().nextInt(3000);
//            System.out.println("sleepTime=" + sleepTime);
//            Thread.sleep(sleepTime);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return "hello " + name + ",当前端口：" + port;
    }

    @Override
    public String getArea(String code) {
        //从Redis缓存中获取信息
        Area area = (Area) redisService.get("getArea" + code);
        //解决高并发问题：双重监测锁
        if (area == null){
            synchronized (this){
                //查询Redis缓存
                area = (Area) redisService.get("getArea" + code);
                if (area == null){
                    //缓存为空，查询数据库
                    area = areaDAO.selectByPrimaryKey(code);
                    logger.info("查询的数据库...");
                    //把查询出来的数据放入到Redis中
                    redisService.set("getArea" + code,area);
                }else {
                    logger.info("查询的缓存...");
                }
            }
        }else {
            logger.info("查询的缓存...");
        }

        if (area != null) {
            //logger.info("当前省市编号【{}】获取信息为【{}】。", code, area.toString());
            return area.toString();
        } else {
            //logger.info("当前省市编号【{}】获取信息不存在。", code);
            return "当前省市编号【" + code + "】获取信息不存在。";
        }
    }
}
