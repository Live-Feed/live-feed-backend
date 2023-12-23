package com.livefeed.livefeedbatch.batch.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
@RequiredArgsConstructor
public class UrlInfoToStringConverter implements Converter<UrlInfo, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convert(UrlInfo source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
