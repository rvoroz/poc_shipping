package com.nybble.propify.carriershipping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OAuthConfiguration {

    @Bean(name = "providerApi")
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean(name = "oAuth")
    RestTemplate getAuthRestTemplate(){
        return new RestTemplate();
    }

}
