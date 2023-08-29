package com.livefeed.livefeedcrawler.batch.configuration;

import com.livefeed.livefeedcrawler.batch.reader.GoogleNewsItemReader;
import com.livefeed.livefeedcrawler.batch.writer.NewsItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class GoogleNewsCrawlJobConfiguration {
    private static final int chunkSize = 10;
    private final GoogleNewsItemReader googleNewsItemReader;
    private final NewsItemWriter newsItemWriter;

    @Bean
    public Job googleNewsCrawlJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("googleNewsCrawlJob", jobRepository)
                .start(googleNewsCrawlStep(jobRepository, transactionManager))
                .build();
    }

    public Step googleNewsCrawlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("googleNewsCrawlStep", jobRepository)
                .<String, String>chunk(chunkSize, transactionManager)
                .reader(googleNewsItemReader)
                .writer(newsItemWriter)
                .build();
    }
}