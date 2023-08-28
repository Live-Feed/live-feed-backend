package com.livefeed.livefeedcrawler.batch.writer;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsItemWriter implements ItemWriter<String> {

    private final KafkaProducerTemplate<String, String> kafkaProducer;

    @Override
    public void write(Chunk<? extends String> chunk) {
        for (String articleUrl : chunk.getItems()) {
            kafkaProducer.sendMessage("LIVEFEED.STREAM.ARTICLE.URL", articleUrl);
        }
    }
}