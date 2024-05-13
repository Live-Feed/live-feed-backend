package com.livefeed.livefeedviewsbatch.batch.domain.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.livefeed.livefeedviewsbatch.batch"})
public class JpaConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.app")
    public DataSourceProperties appDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.app.configuration")
    public HikariDataSource appDataSource(DataSourceProperties appDataSourceProperties) {
        return appDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
}
