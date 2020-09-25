package cn.xie.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(basePackages = "cn.xie",useDefaultFilters = false,includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Controller.class),
@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)})
public class SpringMVCconfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }
}
