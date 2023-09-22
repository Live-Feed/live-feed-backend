package com.livefeed.livefeedparser.kafka.consumer.dto;

import com.livefeed.livefeedparser.parser.Platform;
import com.livefeed.livefeedparser.parser.Theme;

public record ConsumerKeyDto(
        Platform platform,
        Theme theme
) {
}
