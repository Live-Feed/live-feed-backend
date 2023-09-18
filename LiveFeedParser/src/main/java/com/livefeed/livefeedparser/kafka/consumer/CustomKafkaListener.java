package com.livefeed.livefeedparser.kafka.consumer;

import com.livefeed.livefeedcommon.kafka.consumer.KafkaConsumerTemplate;
import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Profile({"prod, local"})
@Component
@Slf4j
@Getter
@RequiredArgsConstructor
public class CustomKafkaListener {

    private final KafkaConsumerTemplate<String, String> consumerTemplate;
    private final KafkaProducerTemplate<Object, Object> producerTemplate;
    private final KafkaTopic kafkaTopic = KafkaTopic.LIVEFEED_URL;

    @KafkaListener(topics = "#{__listener.kafkaTopic.getTopic()}", groupId = "${spring.application.name}")
    public void consume(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            try {
                ProducerRecord<Object, Object> producerRecord = consumerTemplate.processRecord(record);
                producerTemplate.sendMessage(producerRecord);
            } catch (Exception exception) {
                log.error("[exception when url topic consuming] error = ", exception);
                consumerTemplate.sendDlqTopic(kafkaTopic, record);
            }
        }
    }
}
