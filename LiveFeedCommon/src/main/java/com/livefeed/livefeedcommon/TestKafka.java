package com.livefeed.livefeedcommon;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestKafka {

    private final KafkaProducerTemplate<String, String> kafkaProducerTemplate;

    public void send() {
        kafkaProducerTemplate.sendMessage("PRODUCER_TEST", "테스트입니다.");
    }

}
