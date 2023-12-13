package com.livefeed.livefeedbatch.urlcrawler.configuration;

import com.livefeed.livefeedbatch.urlcrawler.reader.NaverNewsItemReader;
import com.livefeed.livefeedbatch.urlcrawler.writer.NewsItemWriter;
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
public class NaverNewsCrawlJobConfiguration {
    private static final int chunkSize = 20;
    private final NaverNewsItemReader naverNewsItemReader;
    private final NewsItemWriter newsItemWriter;

    @Bean
    public Job naverNewsCrawlJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("naverNewsCrawlJob", jobRepository)
                .start(naverNewsCrawlStep(jobRepository, transactionManager))
                .build();
    }

    public Step naverNewsCrawlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("naverNewsCrawlStep", jobRepository)
                .<String, String>chunk(chunkSize, transactionManager)
                .reader(naverNewsItemReader)
                .writer(newsItemWriter)
                .build();
    }
}