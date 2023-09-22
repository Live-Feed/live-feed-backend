package com.livefeed.livefeedparser.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedcommon.kafka.consumer.KafkaConsumerTemplate;
import com.livefeed.livefeedcommon.kafka.exception.ConsumerRecordKeyParsingException;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedparser.parser.ParserProvider;
import com.livefeed.livefeedparser.parser.dto.ParseResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer implements KafkaConsumerTemplate<String, String> {

    private final ObjectMapper objectMapper;
    private final ParserProvider parserProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public ProducerRecord<Object, Object> processRecord(ConsumerRecord<String, String> consumerRecord) {
        ConsumerKeyDto key = readRecordKey(consumerRecord.key());
        String targetParsingUrl = consumerRecord.value();
        log.info("kafka consumerRecord key = {}, value = {}", key, targetParsingUrl);

        // TODO: 2023/09/15 redis에서 이미 확인한 url인지 확인하는 로직 필요

        ParseResultDto parseResultDto = parserProvider.parseWebPage(key, targetParsingUrl);
        return new ProducerRecord<>(KafkaTopic.LIVEFEED_HTML.getTopic(), key, parseResultDto);
    }

    @Override
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

    private ConsumerKeyDto readRecordKey(String consumerRecordKey) {
        try {
            return objectMapper.readValue(consumerRecordKey, ConsumerKeyDto.class);
        } catch (JsonProcessingException e) {
            log.error("kafka key 파싱 에러입니다. key = {}", consumerRecordKey);
            throw new ConsumerRecordKeyParsingException(e.getMessage());
        }
    }
}
