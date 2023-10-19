package com.livefeed.livefeedcommon.kafka.topic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class KafkaTopicTest {

    @DisplayName("KafkaTopic Enum의 topic에 프로듀서가 데이터를 보낼때 실패하면 RETRY 토픽이름을 가져온다.")
    @Test
    void getRetryTopic() {
        // given
        KafkaTopic topic1 = KafkaTopic.LIVEFEED_URL;
        KafkaTopic topic2 = KafkaTopic.LIVEFEED_HTML;
        // when
        String retryTopic1 = topic1.getRetryTopic();
        String retryTopic2 = topic2.getRetryTopic();
        // then
        assertThat(retryTopic1).isEqualTo("LIVEFEED.STREAM.RETRY");
        assertThat(retryTopic2).isEqualTo("LIVEFEED.STREAM.RETRY");
    }

    @DisplayName("KafkaTopic Enum의 topic에 컨슈밍에 실패하면 DLQ 토픽이름을 가져온다.")
    @Test
    void getDlqTopic() {
        // given
        KafkaTopic topic1 = KafkaTopic.LIVEFEED_URL;
        KafkaTopic topic2 = KafkaTopic.LIVEFEED_HTML;
        // when
        String dlqTopic1 = topic1.getDlqTopic();
        String dlqTopic2 = topic2.getDlqTopic();
        // then
        assertThat(dlqTopic1).isEqualTo("LIVEFEED.STREAM.DLQ");
        assertThat(dlqTopic2).isEqualTo("LIVEFEED.STREAM.DLQ");
    }
}