package com.example.product.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.product.external.decoder.CustomErrorDecoder;

@Configuration(proxyBeanMethods = false) 
public class FeinConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder(); 
    }
}