package com.livefeed.livefeedparser.kafka;

import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class KafkaConsumer {

    private final KafkaTopic kafkaTopic = KafkaTopic.LIVEFEED_URL;

    @KafkaListener(topics = "#{__listener.kafkaTopic.getTopic()}", groupId = "${spring.application.name}")
    public void consume(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            String key = record.key();
            String value = record.value();
            log.info("record key = {}, value = {}", key, value);
        }
    }
}
