package com.livefeed.livefeedcommon.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaConsumerTemplate<K, V> {

    void processRecord(ConsumerRecord<K, V> consumerRecord);

}
