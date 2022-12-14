package com.gabozago.backend.common.kakaomap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KakaoMapConfig {

    // HTTP 통신할 때 쓸 객체 만들어주는 간단한 템플릿 메서드
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
