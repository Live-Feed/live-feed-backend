package com.livefeed.livefeedcommon.kafka.configuration;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducer;
import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration<K, V> {

    @Value("${custom-kafka.topic.producer")
    private String targetTopic;

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Bean
    KafkaProducerTemplate<K, V> kafkaProducerTemplate() {
        return new KafkaProducer<>(kafkaTemplate, targetTopic);
    }
}
