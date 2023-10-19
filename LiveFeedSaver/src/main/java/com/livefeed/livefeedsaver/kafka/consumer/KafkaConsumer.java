package com.livefeed.livefeedsaver.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedcommon.kafka.consumer.AbstractKafkaConsumer;
import com.livefeed.livefeedcommon.kafka.record.HtmlTopicKey;
import com.livefeed.livefeedcommon.kafka.record.HtmlTopicValue;
import com.livefeed.livefeedsaver.opensearch.entity.OpensearchArticle;
import com.livefeed.livefeedsaver.opensearch.repository.ArticleOpensearchRepository;
import com.livefeed.livefeedsaver.rdb.entity.Article;
import com.livefeed.livefeedsaver.rdb.service.RdbSaveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer extends AbstractKafkaConsumer<HtmlTopicKey, HtmlTopicValue> {

    private final ArticleOpensearchRepository articleOpensearchRepository;
    private final RdbSaveService rdbSaveService;

    @Autowired
    public KafkaConsumer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate, ArticleOpensearchRepository articleOpensearchRepository, RdbSaveService rdbSaveService) {
        super(objectMapper, kafkaTemplate);
        this.articleOpensearchRepository = articleOpensearchRepository;
        this.rdbSaveService = rdbSaveService;
    }

    @Override
    public ProducerRecord<Object, Object> processRecord(ConsumerRecord<String, String> consumerRecord) {
        HtmlTopicKey key = readRecordKey(consumerRecord.key());
        HtmlTopicValue consumerValueDto = readRecordValue(consumerRecord.value());
        log.info("kafka consumerRecord key = {}, articleTitle = {}", key, consumerValueDto.articleTitle());

        Article article = rdbSaveService.saveArticle(key, consumerValueDto);

        OpensearchArticle opensearchArticle = OpensearchArticle.from(article, key, consumerValueDto);
        articleOpensearchRepository.save(opensearchArticle);
        return null;
    }
}
