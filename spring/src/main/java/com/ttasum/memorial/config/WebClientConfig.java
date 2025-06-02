package com.ttasum.memorial.config;

import jdk.jfr.ContentType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    // 최대 대기 시간 설정
    private final HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(60));

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl("http://127.0.0.1:8000")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
