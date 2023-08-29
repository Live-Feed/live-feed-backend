package com.livefeed.livefeedcommon;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiveFeedCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveFeedCommonApplication.class, args);

    }

}
