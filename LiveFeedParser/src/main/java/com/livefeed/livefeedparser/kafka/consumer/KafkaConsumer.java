package com.livefeed.livefeedparser.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedcommon.kafka.consumer.KafkaConsumerTemplate;
import com.livefeed.livefeedcommon.kafka.exception.ConsumerRecordKeyParsingException;
import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedparser.parser.ArticleTheme;
import com.livefeed.livefeedparser.parser.Parser;
import com.livefeed.livefeedparser.parser.dto.BodyDto;
import com.livefeed.livefeedparser.parser.dto.HeaderDto;
import com.livefeed.livefeedparser.parser.dto.ParseResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer implements KafkaConsumerTemplate<String, String> {

    private final ObjectMapper objectMapper;
    private final Parser parser;
    private final KafkaProducerTemplate<ConsumerKeyDto, ParseResultDto> kafkaProducer;

    @Override
    public void processRecord(ConsumerRecord<String, String> consumerRecord) {
        ConsumerKeyDto key = readRecordKey(consumerRecord.key());
        String targetParsingUrl = consumerRecord.value();
        log.info("kafka consumerRecord key = {}, value = {}", key, targetParsingUrl);

        ArticleTheme articleTheme = findArticleTheme(key);
        ParseResultDto parseResultDto = parser.parseArticle(targetParsingUrl, articleTheme);
        kafkaProducer.sendMessage(KafkaTopic.LIVEFEED_HTML, key, parseResultDto);
    }

    private ConsumerKeyDto readRecordKey(String consumerRecordKey) {
        try {
            return objectMapper.readValue(consumerRecordKey, ConsumerKeyDto.class);
        } catch (JsonProcessingException e) {
            log.error("kafka key 파싱 에러입니다. key = {}", consumerRecordKey);
            throw new ConsumerRecordKeyParsingException(e.getMessage());
        }
    }

    private ArticleTheme findArticleTheme(ConsumerKeyDto key) {
        return ArticleTheme.valueOf(key.theme());
    }
}
