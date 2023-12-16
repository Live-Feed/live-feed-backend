package com.livefeed.livefeedbatch.batch.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource")
public class BatchConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.batch")
    public DataSourceProperties batchDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.batch.configuration")
    public HikariDataSource batchDataSource(
            @Qualifier("batchDataSourceProperties") DataSourceProperties batchDataSourceProperties) {
        return batchDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
}
