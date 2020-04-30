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

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

/**
 * 配置sitemesh拦截规则和调转的父模版地址。
 *
 * @author hongchao zhao at 2019-10-22 16:59
 */
public class MeshsiteFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        //拦截规则，/decorator/default 会被转发  拦截下面的所有，然后转发到固定模板
        builder.addDecoratorPath("/", "/decorator/tabLayout")
                .addDecoratorPath("/welcome", "/decorator/tabLayout")
                .addDecoratorPath("/test", "/decorator/default")
                .addDecoratorPath("/*", "/decorator/common")
                //白名单   代整理，mmp
                .addExcludedPath("/static/**")
                //以下请求不使用模板【所以弹窗页面的调用方法以to开头即可】
                .addExcludedPath("/login")
                .addExcludedPath("/code")
                .addExcludedPath("/checkCode")
                .addExcludedPath("/*/to*")
                .addExcludedPath("/redis/*")
                //自定义标签
                //.addTagRuleBundle(new MyTagRuleBundle())
        ;
    }
}
