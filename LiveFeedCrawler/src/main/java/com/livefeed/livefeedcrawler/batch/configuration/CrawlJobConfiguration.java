package com.livefeed.livefeedcrawler.batch.configuration;

import com.livefeed.livefeedcrawler.batch.GoogleNewsCrawlTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CrawlJobConfiguration {

    private final GoogleNewsCrawlTasklet googleNewsCrawlTasklet;

    @Bean
    public Job googleNewsCrawlJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("googleNewsCrawlJob", jobRepository)
                .start(googleNewsCrawlStep(jobRepository, transactionManager))
                .build();
    }

    public Step googleNewsCrawlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("googleNewsCrawlStep", jobRepository)
                .tasklet(googleNewsCrawlTasklet, transactionManager)
                .build();
    }
}
