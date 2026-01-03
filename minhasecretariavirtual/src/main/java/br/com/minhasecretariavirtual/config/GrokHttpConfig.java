package br.com.minhasecretariavirtual.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.web.client.RestClient;


import reactor.netty.http.client.HttpClient;
import java.time.Duration;

@Configuration
public class GrokHttpConfig {

    @Bean
    public RestClient grokRestClient(
            @Value("${ai.intent.timeout-ms}") int timeoutMs
    ) {

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(timeoutMs));

        return RestClient.builder()
                .requestFactory(new ReactorClientHttpRequestFactory(httpClient))
                .build();
    }
}