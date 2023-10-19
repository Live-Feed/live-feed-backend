package com.livefeed.livefeedcommon.kafka.record;

import com.livefeed.livefeedcommon.kafka.dto.Platform;
import com.livefeed.livefeedcommon.kafka.dto.Service;
import com.livefeed.livefeedcommon.kafka.dto.Theme;

public record UrlTopicKey(
        Service service,
        Platform platform,
        Theme theme
) {
}
