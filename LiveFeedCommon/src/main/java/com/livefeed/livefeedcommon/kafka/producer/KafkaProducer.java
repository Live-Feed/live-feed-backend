package com.livefeed.livefeedcommon.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class KafkaProducer<K, V> implements KafkaProducerTemplate<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;
    private final String targetTopic;

    public KafkaProducer(KafkaTemplate<K, V> kafkaTemplate, String targetTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.targetTopic = targetTopic;
    }

    @Override
    public void sendMessage(String topic, V value) {
        sendMessage(topic, null, value);
    }

    @Override
    public void sendMessage(String topic, K key, V value) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        sendProducerRecord(record);
    }

    private void sendProducerRecord(ProducerRecord<K, V> record) {
        CompletableFuture<SendResult<K, V>> sendResult = kafkaTemplate.send(record);
        sendResult.whenComplete((result, ex) -> {
            if (ex != null) {
                // TODO: 2023/08/18 실제 에러 토픽으로 보낼지 혹은 로그만 작성할지 논의 필요
                log.error("kafka producer send error", ex);
            }
        });
    }
}
