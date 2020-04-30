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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 多视图实现的视图解析器，将HTML和JSP区分开来解析。
 *
 * @author hongchao zhao at 2019-7-30 14:57
 */
@Configuration
public class ViewResolverConfiguration {

    @Configuration//用来定义 DispatcherServlet 应用上下文中的 bean
    @EnableWebMvc
    @ComponentScan("com.distressed.asset")
    public class WebConfig implements WebMvcConfigurer {

        @Bean
        public ViewResolver viewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            //结尾一定要以/结尾，不然找不到对应的文件
            resolver.setPrefix("/WEB-INF/views/");
            resolver.setSuffix(".jsp");
            resolver.setViewNames("*");
            resolver.setOrder(2);
            return resolver;
        }

//        @Bean
//        public ITemplateResolver templateResolver() {
//            SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//            templateResolver.setTemplateMode("HTML5");
//            templateResolver.setPrefix("/WEB-INF/");
//            templateResolver.setSuffix(".html");
//            templateResolver.setCharacterEncoding("utf-8");
//            templateResolver.setCacheable(false);
//            return templateResolver;
//        }
//
//        @Bean
//        public SpringTemplateEngine templateEngine() {
//            SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//            templateEngine.setTemplateResolver(templateResolver());
//            // templateEngine
//            return templateEngine;
//        }
//
//        @Bean
//        public ThymeleafViewResolver viewResolverThymeLeaf() {
//            ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//            viewResolver.setTemplateEngine(templateEngine());
//            viewResolver.setCharacterEncoding("utf-8");
//            viewResolver.setOrder(1);
//            viewResolver.setViewNames(new String[]{"html/*"});
//            return viewResolver;
//        }

        @Override
        public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
            configurer.enable();
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");
            registry.addResourceHandler("/scripts/**").addResourceLocations("/WEB-INF/static/scripts/");
        }


    }
}
