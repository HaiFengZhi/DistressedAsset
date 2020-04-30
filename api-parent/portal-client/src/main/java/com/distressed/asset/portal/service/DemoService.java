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

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 测试接口。
 *
 * @author hongchao zhao at 2020-4-30 15:24
 */
@FeignClient(value = "portal-server",contextId = "demoService" )
public interface DemoService {

    /**
     * 定义接口方法；
     *
     * @param name 用户名。
     * @return 返回接口数据；
     */
    @RequestMapping(value = "/sayHi",method = RequestMethod.GET)
    String sayHi(@RequestParam(value = "name") String name);

    /**
     * 根据编号获取省市信息。[测试方法]
     *
     * @param code 省市编号。
     * @return 省市信息。
     */
    @PostMapping("/getArea")
    String getArea(@RequestParam(value = "code") String code);



}
