package com.livefeed.livefeedcommon.kafka.producer;


import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaProducerTemplate<K, V> {

    void sendMessage(KafkaTopic kafkaTopic, V value);

    void sendMessage(KafkaTopic kafkaTopic, K key, V value);

    void sendMessage(ProducerRecord<K, V> producerRecord);

    void sendDlqTopic(KafkaTopic kafkaTopic, ConsumerRecord<K, V> consumerRecord);
}
