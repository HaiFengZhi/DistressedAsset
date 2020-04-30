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

package com.distressed.asset.admin.config.sitemesh;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * sitemesh的装饰模板，类似于sitemesh-decorators.xml文件。
 *
 * @author hongchao zhao at 2019-10-22 17:19
 */
@Controller
@RequestMapping("/decorator")
public class MeshsiteConfig {

    @RequestMapping("default")
    public String defaultDecorator() {
        return "/templates/indexLayout";
    }

    @RequestMapping("common")
    public String commonDecorator() {
        return "/templates/common";
    }

    @RequestMapping("tabLayout")
    public String testDecorator() {
        return "/templates/tabLayout";
    }



}
