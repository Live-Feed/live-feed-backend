package com.livefeed.livefeedcommon.kafka.producer;

import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9094", "port=9094" })
class KafkaProducerTest {

    @Autowired
    private KafkaProducerTemplate<String, String> kafkaProducer;

    @Test
    public void testErrorScenario() {
        // 메시지 전송 시도
        kafkaProducer.sendMessage(KafkaTopic.LIVEFEED_URL, "테스트 메시지1");
        kafkaProducer.sendMessage(KafkaTopic.LIVEFEED_HTML, "테스트 메시지2");
    }

    @Test
    @DisplayName("컨슈머 로직이 실패할 경우 dlq 토픽으로 실패한 consumerRecord를 보낸다.")
    void sendDlqTopic() {
        // given
        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>(KafkaTopic.LIVEFEED_URL.getTopic(), 0, 0, null, "테스트 메시지");
        // when
        // then
        kafkaProducer.sendDlqTopic(KafkaTopic.LIVEFEED_URL, consumerRecord);
    }
}