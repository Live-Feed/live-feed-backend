package com.livefeed.livefeedsaver.kafka.consumer.dto;

import com.livefeed.livefeedsaver.common.dto.Platform;
import com.livefeed.livefeedsaver.common.dto.Service;
import com.livefeed.livefeedsaver.common.dto.Theme;

public record ConsumerKeyDto(
        Service service,
        Platform platform,
        Theme theme
) {
}
