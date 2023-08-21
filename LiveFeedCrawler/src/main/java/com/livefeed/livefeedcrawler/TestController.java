package com.livefeed.livefeedcrawler;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawler-hello")
@RequiredArgsConstructor
public class TestController {

    private final KafkaProducerTemplate<String, String> kafkaProducer;

    @GetMapping
    public String testKafkaController() {
        kafkaProducer.sendMessage("PRODUCER_CRAWLER_TEST", "자동주입 테스트입니다.222");
        return "hello";
    }
}
