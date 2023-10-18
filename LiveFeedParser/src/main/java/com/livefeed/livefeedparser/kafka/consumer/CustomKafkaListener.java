package com.livefeed.livefeedparser.kafka.consumer;

import com.livefeed.livefeedcommon.kafka.consumer.KafkaConsumerTemplate;
import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerValueDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
@RequiredArgsConstructor
public class CustomKafkaListener {

//    private final KafkaConsumerTemplate<String, String> consumerTemplate;
    private final KafkaConsumer kafkaConsumer;
    private final KafkaProducerTemplate<Object, Object> producerTemplate;
    private final KafkaTopic kafkaTopic = KafkaTopic.LIVEFEED_URL;

    @KafkaListener(topics = "#{__listener.kafkaTopic.getTopic()}", groupId = "${spring.application.name}")
    public void consume(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            try {
//                ProducerRecord<Object, Object> producerRecord = consumerTemplate.processRecord(record);
                ProducerRecord<Object, Object> producerRecord = kafkaConsumer.processRecord(record);
                if (producerRecord != null) {
                    producerTemplate.sendMessage(producerRecord);
                }
            } catch (Exception exception) {
                log.error("[exception when url topic consuming] error = ", exception);
                kafkaConsumer.sendDlqTopic(kafkaTopic, record);
            }
        }
    }
}
