package com.livefeed.livefeedcrawler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class AutoConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;


    @Test
    @DisplayName("kafka producer 자동 주입 관련 custom.kafka.producer.is-enabled 값이 true라면 주입한다.")
    void autoConfiguration() {
        // given
        // when
        Object kafkaProducerTemplate = applicationContext.getBean("kafkaProducerTemplate");
        // then
        assertThat(kafkaProducerTemplate).isNotNull();
    }
}
