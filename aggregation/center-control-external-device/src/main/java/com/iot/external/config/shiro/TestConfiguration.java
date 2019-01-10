package com.iot.external.config.shiro;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiListing;

@Configuration
public class TestConfiguration {

    @Bean(name = "test2222")
    public String test2222(){
        System.out.println("你三大爷的");
        return null;
    }
}
