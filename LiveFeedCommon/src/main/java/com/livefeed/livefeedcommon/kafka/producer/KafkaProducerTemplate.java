package com.livefeed.livefeedcommon.kafka.producer;

import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface KafkaProducerTemplate<K, V> {

    void sendMessage(String topic, V value);

    void sendMessage(String topic, K key, V value);
}
