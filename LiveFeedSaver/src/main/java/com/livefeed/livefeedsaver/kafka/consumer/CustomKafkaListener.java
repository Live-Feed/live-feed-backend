package com.livefeed.livefeedsaver.kafka.consumer;

import com.livefeed.livefeedcommon.kafka.consumer.KafkaConsumerTemplate;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
@RequiredArgsConstructor
public class CustomKafkaListener {

    private final KafkaConsumerTemplate<String, String> consumerTemplate;
    private final KafkaTopic kafkaTopic = KafkaTopic.LIVEFEED_HTML;

    @KafkaListener(topics = "#{__listener.kafkaTopic.getTopic()}", groupId = "${spring.application.name}")
    public void consume(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            try {
                consumerTemplate.processRecord(record);
            } catch (Exception exception) {
                log.error("[exception when saving article] error = ", exception);
                consumerTemplate.sendDlqTopic(kafkaTopic, record);
            }
        }
    }
}
