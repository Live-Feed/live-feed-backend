package com.livefeed.livefeedcrawler;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawler-hello")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final KafkaProducerTemplate<String, String> kafkaProducer;

    @GetMapping
    public String testKafkaController() {
        log.info("kafka producer test");
        for (int i = 10; i < 20; i++) {
            kafkaProducer.sendMessage(KafkaTopic.LIVEFEED_URL, String.valueOf(i), "crawler ci 테스트 입니다. " + i);
        }
        log.info("kafka producer test end");
        return "hello";
    }
}
