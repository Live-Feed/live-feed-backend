package com.livefeed.livefeedparser.kafka.consumer.dto;

public record ConsumerKeyDto(
        Service service,
        Platform platform,
        Theme theme
) {
}
