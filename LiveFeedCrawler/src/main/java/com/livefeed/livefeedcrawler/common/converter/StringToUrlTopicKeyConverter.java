package com.livefeed.livefeedcrawler.common.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedcommon.kafka.record.UrlTopicKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
@RequiredArgsConstructor
public class StringToUrlTopicKeyConverter implements Converter<String, UrlTopicKey> {

    private final ObjectMapper objectMapper;

    @Override
    public UrlTopicKey convert(String source) {
        return objectMapper.convertValue(source, UrlTopicKey.class);
    }
}
