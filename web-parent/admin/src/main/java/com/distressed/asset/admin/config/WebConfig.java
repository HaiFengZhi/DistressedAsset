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

package com.distressed.asset.admin.config;

import com.distressed.asset.admin.config.sitemesh.MeshsiteFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 添加公共组件。
 *
 * @author hongchao zhao at 2019-10-22 17:08
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //添加siteMesh框架
    @Bean
    public FilterRegistrationBean siteMeshFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        MeshsiteFilter siteMeshFilter = new MeshsiteFilter();
        filter.setFilter(siteMeshFilter);
        return filter;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//            //添加拦截器
//            InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
//
//            //排除的路径
//            registration.excludePathPatterns("/login");
//            registration.excludePathPatterns("/logout");
//            //将这个controller放行，否则拦截器会执行两次
//            registration.excludePathPatterns("/error");
//            //拦截全部
//            registration.addPathPatterns("/**");
//
//    }

}
