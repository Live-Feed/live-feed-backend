package com.livefeed.livefeedcrawler.batch.writer;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import com.livefeed.livefeedcommon.kafka.record.UrlTopicKey;
import com.livefeed.livefeedcommon.kafka.record.UrlTopicValue;
import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
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

    @Value("#{jobParameters[urlTopicKey]}")
    private UrlTopicKey urlTopicKey;

    private final KafkaProducerTemplate<UrlTopicKey, UrlTopicValue> kafkaProducer;

    @Override
    public void write(Chunk<? extends String> chunk) {
        for (String articleUrl : chunk.getItems()) {
            kafkaProducer.sendMessage(KafkaTopic.LIVEFEED_URL, urlTopicKey, new UrlTopicValue(articleUrl));
        }
    }
}