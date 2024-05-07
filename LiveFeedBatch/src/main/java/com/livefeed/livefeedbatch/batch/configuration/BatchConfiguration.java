package com.livefeed.livefeedbatch.batch.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedbatch.batch.common.converter.StringToUrlInfoConverter;
import com.livefeed.livefeedbatch.batch.common.converter.UrlInfoToStringConverter;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.converter.LocalDateTimeToStringConverter;
import org.springframework.batch.core.converter.StringToLocalDateTimeConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", conversionServiceRef = "batchConversionService")
@RequiredArgsConstructor
public class BatchConfiguration {

    private final ObjectMapper objectMapper;

    @Bean
    @ConfigurationProperties("spring.datasource.batch")
    public DataSourceProperties batchDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.batch.configuration")
    @Qualifier("batchDataSource")
    public HikariDataSource batchDataSource(
            @Qualifier("batchDataSourceProperties") DataSourceProperties batchDataSourceProperties) {
        return batchDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public ConfigurableConversionService batchConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new LocalDateTimeToStringConverter());
        conversionService.addConverter(new StringToLocalDateTimeConverter());
        conversionService.addConverter(UrlInfo.class, String.class, new UrlInfoToStringConverter(objectMapper));
        conversionService.addConverter(String.class, UrlInfo.class, new StringToUrlInfoConverter(objectMapper));
        return conversionService;
    }
}
