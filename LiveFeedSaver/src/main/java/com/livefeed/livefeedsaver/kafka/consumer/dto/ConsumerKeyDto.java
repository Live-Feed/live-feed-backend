package com.livefeed.livefeedsaver.kafka.consumer.dto;

import com.livefeed.livefeedsaver.common.dto.Platform;
import com.livefeed.livefeedsaver.common.dto.Theme;

public record ConsumerKeyDto(
        Platform platform,
        Theme theme
) {
}
