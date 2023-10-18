package com.livefeed.livefeedsaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
public class LiveFeedSaverApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveFeedSaverApplication.class, args);
    }

}
