package com.livefeed.livefeedcommon.kafka.configuration;

import com.livefeed.livefeedcommon.kafka.producer.KafkaProducer;
import com.livefeed.livefeedcommon.kafka.producer.KafkaProducerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@AutoConfiguration
@ConditionalOnProperty(name = "custom.kafka.producer.is-enabled", havingValue = "true")
@RequiredArgsConstructor
public class KafkaProducerAutoConfiguration<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Bean
    @ConditionalOnMissingBean
    public KafkaProducerTemplate<K, V> kafkaProducerTemplate() {
        return new KafkaProducer<>(kafkaTemplate);
    }
}
