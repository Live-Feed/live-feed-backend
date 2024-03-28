package com.livefeed.livefeedservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = ReactiveElasticsearchRepositoriesAutoConfiguration.class)
public class LiveFeedServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveFeedServiceApplication.class, args);
    }

}
