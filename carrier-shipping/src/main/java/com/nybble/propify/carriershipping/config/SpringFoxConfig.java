package com.nybble.propify.carriershipping.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nybble.propify.carriershipping.controller"))
                .build()
                .securitySchemes(securitySchemes());
    }

    private static List<SecurityScheme> securitySchemes() {
         return  Arrays.asList(new ApiKey("Bearer", "Authorization", "header"));
    }
}
