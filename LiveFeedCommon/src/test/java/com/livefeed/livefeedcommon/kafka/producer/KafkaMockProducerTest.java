package com.livefeed.livefeedcommon.kafka.producer;

import com.livefeed.livefeedcommon.kafka.topic.KafkaTopic;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.mock.MockProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9094", "port=9094" })
class KafkaMockProducerTest {

    @DisplayName("test")
    @Test
    void tst() {
        // KafkaTemplate 생성
//        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);

        KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
//        CompletableFuture<SendResult<String, String>> completableFuture = new CompletableFuture<>();

        CompletableFuture<SendResult<String, String>> completableFuture = mock(CompletableFuture.class);

        // whenComplete 메서드에 전달되는 함수(콜백) 모킹
        Mockito.doAnswer(invocation -> {
            BiConsumer<String, Throwable> biConsumer = invocation.getArgument(0);
            // 모킹한 콜백 함수를 호출
            biConsumer.accept("Success Result", null);
            return null;
        }).when(completableFuture).whenComplete(any());

        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(completableFuture);


        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(kafkaTemplate);
        kafkaProducer.sendMessage(KafkaTopic.LIVEFEED_HTML, "test");
    }
}