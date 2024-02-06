package com.livefeed.livefeedbatch.batch.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedbatch.batch.common.converter.StringToUrlInfoConverter;
import com.livefeed.livefeedbatch.batch.common.converter.UrlInfoToStringConverter;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.zaxxer.hikari.HikariDataSource;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.converter.LocalDateTimeToStringConverter;
import org.springframework.batch.core.converter.StringToLocalDateTimeConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import static org.hibernate.internal.util.collections.CollectionHelper.asProperties;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", conversionServiceRef = "batchConversionService")
public class BatchConfig {
    // TODO: 2023/12/18 추후 하나의 객체로 통일
    private final ObjectMapper objectMapper = new ObjectMapper();

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

    @Bean
    public SchedulerFactoryBean quartzScheduler(QuartzProperties properties,
                                                @Qualifier("batchDataSource") HikariDataSource dataSource,
                                                JobDetail jobDetail,
                                                Trigger trigger
    ) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        // 다른 Quartz 설정 추가
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        schedulerFactoryBean.setJobFactory(jobFactory);
        if (properties.getSchedulerName() != null) {
            schedulerFactoryBean.setSchedulerName(properties.getSchedulerName());
        }
        schedulerFactoryBean.setAutoStartup(properties.isAutoStartup());
        schedulerFactoryBean.setStartupDelay((int) properties.getStartupDelay().getSeconds());
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(properties.isWaitForJobsToCompleteOnShutdown());
        schedulerFactoryBean.setOverwriteExistingJobs(properties.isOverwriteExistingJobs());
        if (!properties.getProperties().isEmpty()) {
            schedulerFactoryBean.setQuartzProperties(asProperties(properties.getProperties()));
        }
        schedulerFactoryBean.setJobDetails(jobDetail);
        schedulerFactoryBean.setTriggers(trigger);
        return schedulerFactoryBean;
    }

    // quartz 초기화 테이블 생성시에도 batchDataSource를 이용해야 하기 때문에 설정
    // local 환경에만 필요하고 prod에서는 테이블을 미리 생성할것이기 때문에 필요 없습니다.
    @Profile("local")
    @Bean
    public QuartzDataSourceScriptDatabaseInitializer quartzDataSourceScriptDatabaseInitializer(
            @Qualifier("batchDataSource") HikariDataSource dataSource,
            QuartzProperties properties) {
        return new QuartzDataSourceScriptDatabaseInitializer(dataSource, properties);
    }
}
