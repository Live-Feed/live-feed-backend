package com.livefeed.livefeedcrawler.batch.configuration;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@NonNullApi
@Configuration
public class TestJobConfiguration {

    @Bean
    public Job testJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("testJob", jobRepository)
                .start(new StepBuilder("testStep", jobRepository)
                        .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager)
                        .build())
                .build();
    }
}
