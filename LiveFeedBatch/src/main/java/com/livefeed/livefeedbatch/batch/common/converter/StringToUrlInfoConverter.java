package com.livefeed.livefeedbatch.batch.common.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
@RequiredArgsConstructor
public class StringToUrlInfoConverter implements Converter<String, UrlInfo> {

    private final ObjectMapper objectMapper;

    @Override
    public UrlInfo convert(String source) {
        return objectMapper.convertValue(source, UrlInfo.class);
    }
}
