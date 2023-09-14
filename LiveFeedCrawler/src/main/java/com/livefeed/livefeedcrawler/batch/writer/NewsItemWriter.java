package com.livefeed.livefeedcrawler.batch.writer;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import com.livefeed.livefeedcrawler.common.NewsKey;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class NewsItemWriter implements ItemWriter<String> {

    @Value("#{jobParameters[platform]}")
    private String platform;

    @Value("#{jobParameters[theme]}")
    private String theme;

    private final KafkaProducerTemplate<NewsKey, String> kafkaProducer;

    @Override
    public void write(Chunk<? extends String> chunk) {
        NewsKey newsKey = NewsKey.of(platform, theme);
        for (String articleUrl : chunk.getItems()) {
            kafkaProducer.sendMessage(KafkaTopic.LIVEFEED_URL, newsKey, articleUrl);
        }
    }
}