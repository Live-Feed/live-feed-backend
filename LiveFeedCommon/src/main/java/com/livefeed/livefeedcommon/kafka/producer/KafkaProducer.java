package com.livefeed.livefeedcommon.kafka.producer;


import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class KafkaProducer<K, V> implements KafkaProducerTemplate<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public void sendMessage(KafkaTopic kafkaTopic, V value) {
        sendMessage(kafkaTopic, null, value);
    }

    @Override
    public void sendMessage(KafkaTopic kafkaTopic, K key, V value) {
        ProducerRecord<K, V> producerRecord = new ProducerRecord<>(kafkaTopic.getTopic(), key, value);
        sendMessage(producerRecord);
    }

    @Override
    public void sendMessage(ProducerRecord<K, V> producerRecord) {
        sendProducerRecord(producerRecord);
    }

    @Override
    public void sendDlqTopic(KafkaTopic kafkaTopic, ConsumerRecord<K, V> consumerRecord) {
        String dlqTopic = kafkaTopic.getDlqTopic();
        ProducerRecord<K, V> producerRecord = new ProducerRecord<>(dlqTopic, (K) kafkaTopic.getTopic(), consumerRecord.value());
        CompletableFuture<SendResult<K, V>> sendResult = kafkaTemplate.send(producerRecord);

        sendResult.whenComplete((result, ex) -> {
            KafkaProducerException exception = (KafkaProducerException) ex;
            if (ex != null) {
                log.error("[kafka producer send dlq error] record topic = {}, timestamp = {}, partition = {}",
                        exception.getFailedProducerRecord().topic(),
                        exception.getFailedProducerRecord().timestamp(),
                        exception.getFailedProducerRecord().partition());
            }
        });
    }

    private void sendProducerRecord(ProducerRecord<K, V> record) {
        CompletableFuture<SendResult<K, V>> sendResult = kafkaTemplate.send(record);
        sendResult.whenComplete((result, ex) -> {
            if (ex != null) {
                KafkaProducerException exception = (KafkaProducerException) ex;
                log.error("[kafka producer send error] record topic = {}, timestamp = {}, partition = {}",
                        exception.getFailedProducerRecord().topic(),
                        exception.getFailedProducerRecord().timestamp(),
                        exception.getFailedProducerRecord().partition());

                sendRetryTopic(exception);
            } else {
                log.info("[kafka producer send] success topic = {}, offset = {}, partition = {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition());
            }
        });
    }

    private void sendRetryTopic(KafkaProducerException exception) {
        for (KafkaTopic kafkaTopic : KafkaTopic.values()) {
            if (kafkaTopic.getTopic().equals(exception.getFailedProducerRecord().topic())) {
                sendRetryTopic(kafkaTopic, exception);
            }
        }
    }

    private void sendRetryTopic(KafkaTopic kafkaTopic, KafkaProducerException kafkaProducerException) {
        ProducerRecord<K, V> failedRecord = kafkaProducerException.getFailedProducerRecord();
        String retryTopic = kafkaTopic.getRetryTopic();
        ProducerRecord<K, V> retryRecord = new ProducerRecord<>(retryTopic, failedRecord.key(), failedRecord.value());
        CompletableFuture<SendResult<K, V>> sendResult = kafkaTemplate.send(retryRecord);

        sendResult.whenComplete((result, ex) -> {
            KafkaProducerException exception = (KafkaProducerException) ex;
            if (ex != null) {
                log.error("[kafka producer send retry error] record topic = {}, timestamp = {}, partition = {}",
                        exception.getFailedProducerRecord().topic(),
                        exception.getFailedProducerRecord().timestamp(),
                        exception.getFailedProducerRecord().partition());
            }
        });
    }
}
