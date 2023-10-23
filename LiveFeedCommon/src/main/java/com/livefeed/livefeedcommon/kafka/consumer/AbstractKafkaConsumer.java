package com.livefeed.livefeedcommon.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.livefeed.livefeedcommon.kafka.exception.ConsumerRecordKeyParsingException;
import com.livefeed.livefeedcommon.kafka.exception.ConsumerRecordValueParsingException;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
abstract public class AbstractKafkaConsumer<K, V> {

    private Class<K> key;
    private Class<V> value;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AbstractKafkaConsumer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;

        TypeToken<K> keyTypeToken = new TypeToken<K>(getClass()) {};
        TypeToken<V> valueTypeToken = new TypeToken<V>(getClass()) {};

        key = (Class<K>) keyTypeToken.getRawType();
        value = (Class<V>) valueTypeToken.getRawType();
    }

    abstract public ProducerRecord<Object, Object> processRecord(ConsumerRecord<String, String> consumerRecord);

    public void sendDlqTopic(KafkaTopic kafkaTopic, ConsumerRecord<String, String> consumerRecord) {
        String dlqTopic = kafkaTopic.getDlqTopic();
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(dlqTopic, kafkaTopic.getTopic(), consumerRecord.value());
        CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(producerRecord);

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

    protected K readRecordKey(String consumerRecordKey) {
        try {
            return objectMapper.readValue(consumerRecordKey, key);
        } catch (Exception e) {
            log.error("kafka key 파싱 에러입니다. key = {}", consumerRecordKey);
            throw new ConsumerRecordKeyParsingException(e.getMessage());
        }
    }

    protected V readRecordValue(String consumerRecordValue) {
        try {
            return objectMapper.readValue(consumerRecordValue, value);
        } catch (JsonProcessingException e) {
            log.error("kafka value 파싱 에러입니다. value = {}", consumerRecordValue);
            throw new ConsumerRecordValueParsingException(e.getMessage());
        }
    }
}
