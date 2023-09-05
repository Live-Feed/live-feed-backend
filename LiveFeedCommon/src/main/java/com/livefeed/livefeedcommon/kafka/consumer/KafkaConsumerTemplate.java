package com.livefeed.livefeedcommon.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaConsumerTemplate<K, V> {

    ProducerRecord<K, V> processRecord(ConsumerRecord<K, V> consumerRecord);

}
