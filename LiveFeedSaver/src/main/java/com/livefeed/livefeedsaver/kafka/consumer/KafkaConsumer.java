package com.livefeed.livefeedsaver.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedcommon.kafka.consumer.AbstractKafkaConsumer;
import com.livefeed.livefeedcommon.kafka.record.HtmlTopicKey;
import com.livefeed.livefeedcommon.kafka.record.HtmlTopicValue;
import com.livefeed.livefeedsaver.elasticsearch.entity.ElasticsearchArticle;
import com.livefeed.livefeedsaver.elasticsearch.repository.ArticleElasticsearchRepository;
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

    private final ArticleElasticsearchRepository articleElasticsearchRepository;
    private final RdbSaveService rdbSaveService;

    @Autowired
    public KafkaConsumer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate, ArticleElasticsearchRepository articleElasticsearchRepository, RdbSaveService rdbSaveService) {
        super(objectMapper, kafkaTemplate);
        this.articleElasticsearchRepository = articleElasticsearchRepository;
        this.rdbSaveService = rdbSaveService;
    }

    @Override
    public ProducerRecord<Object, Object> processRecord(ConsumerRecord<String, String> consumerRecord) {
        HtmlTopicKey key = readRecordKey(consumerRecord.key());
        HtmlTopicValue consumerValueDto = readRecordValue(consumerRecord.value());
        log.info("kafka consumerRecord key = {}, articleTitle = {}", key, consumerValueDto.articleTitle());

        Article article = rdbSaveService.saveArticle(key, consumerValueDto);

        ElasticsearchArticle elasticsearchArticle = ElasticsearchArticle.from(article, key, consumerValueDto);
        articleElasticsearchRepository.save(elasticsearchArticle);
        return null;
    }
}
