package com.livefeed.livefeedsaver.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedcommon.kafka.consumer.KafkaConsumerTemplate;
import com.livefeed.livefeedcommon.kafka.exception.ConsumerRecordKeyParsingException;
import com.livefeed.livefeedcommon.kafka.exception.ConsumerRecordValueParsingException;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerValueDto;
import com.livefeed.livefeedsaver.opensearch.entity.OpensearchArticle;
import com.livefeed.livefeedsaver.opensearch.repository.ArticleOpensearchRepository;
import com.livefeed.livefeedsaver.rdb.entity.Article;
import com.livefeed.livefeedsaver.rdb.service.RdbSaveService;
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
    private final ArticleOpensearchRepository articleOpensearchRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RdbSaveService rdbSaveService;

    @Override
    public ProducerRecord<Object, Object> processRecord(ConsumerRecord<String, String> consumerRecord) {
        ConsumerKeyDto key = readRecordKey(consumerRecord.key());
        ConsumerValueDto consumerValueDto = readRecordValue(consumerRecord.value());
        log.info("kafka consumerRecord key = {}, articleTitle = {}", key, consumerValueDto.articleTitle());

        Article article = rdbSaveService.saveArticle(key, consumerValueDto);

        OpensearchArticle opensearchArticle = OpensearchArticle.from(article, key, consumerValueDto);
        articleOpensearchRepository.save(opensearchArticle);
        return null;
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

    private ConsumerValueDto readRecordValue(String consumerRecordValue) {
        try {
            return objectMapper.readValue(consumerRecordValue, ConsumerValueDto.class);
        } catch (JsonProcessingException e) {
            log.error("kafka value 파싱 에러입니다. value = {}", consumerRecordValue);
            throw new ConsumerRecordValueParsingException(e.getMessage());
        }
    }
}
