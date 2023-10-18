package com.livefeed.livefeedparser.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedcommon.kafka.consumer.AbstractKafkaConsumer;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerValueDto;
import com.livefeed.livefeedparser.parser.ParserProvider;
import com.livefeed.livefeedparser.parser.dto.ParseResultDto;
import com.livefeed.livefeedparser.redis.operations.RedisOperations;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer extends AbstractKafkaConsumer<ConsumerKeyDto, ConsumerValueDto> {

    private final ParserProvider parserProvider;
    private final RedisOperations<String, Boolean> redisOperations;

    @Autowired
    public KafkaConsumer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate, ParserProvider parserProvider, RedisOperations<String, Boolean> redisOperations) {
        super(objectMapper, kafkaTemplate);
        this.parserProvider = parserProvider;
        this.redisOperations = redisOperations;
    }

    @Override
    public ProducerRecord<Object, Object> processRecord(ConsumerRecord<String, String> consumerRecord) {
        ConsumerKeyDto key = readRecordKey(consumerRecord.key());
        ConsumerValueDto consumerValueDto = readRecordValue(consumerRecord.value());
        log.info("kafka consumerRecord key = {}, value = {}", key, consumerValueDto.url());

        if (redisOperations.isDuplicate(consumerValueDto.url(), true)) {
            return null;
        }

        ParseResultDto parseResultDto = parserProvider.parseWebPage(key, consumerValueDto.url());
        return new ProducerRecord<>(KafkaTopic.LIVEFEED_HTML.getTopic(), key, parseResultDto);
    }
}
