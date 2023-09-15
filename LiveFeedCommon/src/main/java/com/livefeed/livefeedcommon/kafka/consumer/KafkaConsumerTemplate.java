package com.livefeed.livefeedcommon.kafka.consumer;

import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaConsumerTemplate<K, V> {

    ProducerRecord<Object, Object> processRecord(ConsumerRecord<K, V> consumerRecord);

    void sendDlqTopic(KafkaTopic kafkaTopic, ConsumerRecord<K, V> consumerRecord);
}
