package com.company.socialmedia.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ANT_PATH_EXPRESSION = "uploads/**";
        registry
                .addResourceHandler(ANT_PATH_EXPRESSION)
                .addResourceLocations("file:resources/", "file:uploads/");
    }
}
