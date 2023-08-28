package com.livefeed.livefeedcommon.kafka.producer;


import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;

public interface KafkaProducerTemplate<K, V> {

    void sendMessage(KafkaTopic kafkaTopic, V value);

    void sendMessage(KafkaTopic kafkaTopic, K key, V value);
}
