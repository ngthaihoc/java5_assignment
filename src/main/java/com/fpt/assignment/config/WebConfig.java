package com.fpt.assignment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve ảnh avatar từ C:/uploads/avatars/ tại URL /uploads/avatars/**
        registry.addResourceHandler("/uploads/avatar/**")
                .addResourceLocations("file:C:/uploads/avatar/");

        // Serve ảnh sách từ C:/uploads/books/ tại URL /uploads/books/**
        registry.addResourceHandler("/uploads/books/**")
                .addResourceLocations("file:C:/uploads/books/");
    }
}