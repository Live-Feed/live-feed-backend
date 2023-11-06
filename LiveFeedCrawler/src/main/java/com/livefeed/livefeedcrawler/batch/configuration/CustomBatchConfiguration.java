package com.livefeed.livefeedcrawler.batch.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedcommon.kafka.record.UrlTopicKey;
import com.livefeed.livefeedcrawler.common.converter.UrlTopicKeyToStringConverter;
import com.livefeed.livefeedcrawler.common.converter.StringToUrlTopicKeyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;

@Configuration
@RequiredArgsConstructor
public class CustomBatchConfiguration extends DefaultBatchConfiguration {

    private final ObjectMapper objectMapper;

    @Override
    protected ConfigurableConversionService getConversionService() {
        ConfigurableConversionService conversionService = super.getConversionService();
        conversionService.addConverter(UrlTopicKey.class, String.class, new UrlTopicKeyToStringConverter(objectMapper));
        conversionService.addConverter(String.class, UrlTopicKey.class, new StringToUrlTopicKeyConverter(objectMapper));
        return conversionService;
    }
}
