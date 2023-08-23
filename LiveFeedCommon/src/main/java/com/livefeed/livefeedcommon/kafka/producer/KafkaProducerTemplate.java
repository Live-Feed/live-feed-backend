package com.livefeed.livefeedcommon.kafka.producer;


public interface KafkaProducerTemplate<K, V> {

    void sendMessage(String topic, V value);

    void sendMessage(String topic, K key, V value);
}
