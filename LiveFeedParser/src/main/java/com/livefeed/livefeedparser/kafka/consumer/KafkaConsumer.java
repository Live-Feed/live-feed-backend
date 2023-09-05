package com.livefeed.livefeedparser.kafka.consumer;

import com.livefeed.livefeedcommon.kafka.consumer.KafkaConsumerTemplate;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer implements KafkaConsumerTemplate<String, String> {

    @Override
    public ProducerRecord<String, String> processRecord(ConsumerRecord<String, String> consumerRecord) {
        String value = consumerRecord.value();
        log.info("processRecord value = {}", value);
        return new ProducerRecord<>(KafkaTopic.LIVEFEED_HTML.getTopic(), value);
    }
}
