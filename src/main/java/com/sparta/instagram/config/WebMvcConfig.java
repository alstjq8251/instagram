package com.sparta.instagram.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
//                .allowedOrigins("*")
                .allowedOrigins("*")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name())
//                .allowedOrigins("http://**")
//                .allowedOrigins("https://**")
//                .allowedMethods("GET", "POST", "HEAD", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("Authorization , Refresh-Token")
                .exposedHeaders("Authorization , Refresh-Token")
                .maxAge(3600L);
//
//        .allowedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, "accessToken", "CorrelationId", "source")
//                .exposedHeaders(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, "accessToken", "CorrelationId", "source")

    }
}
//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry){
//        corsRegistry.addMapping("/**")
//                .allowedOrigins("http://리액트ip주소:3000")
//                .allowedMethods("OPTIONS", "GET", "POST", "PATCH", "DELETE");
//    }

