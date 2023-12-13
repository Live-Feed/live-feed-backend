package com.livefeed.livefeedbatch.configuration;

import com.livefeed.livefeedbatch.urlcrawler.reader.NaverNewsUrlReader;
import com.livefeed.livefeedbatch.urlcrawler.writer.NewsUrlWriter;
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
public class NaverNewsUrlCrawlJobConfiguration {
    private static final int chunkSize = 20;
    private final NaverNewsUrlReader naverNewsUrlReader;
    private final NewsUrlWriter newsUrlWriter;

    @Bean
    public Job naverNewsCrawlJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("naverNewsCrawlJob", jobRepository)
                .start(naverNewsCrawlStep(jobRepository, transactionManager))
                .build();
    }

    public Step naverNewsCrawlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("naverNewsUrlCrawlStep", jobRepository)
                .<String, String>chunk(chunkSize, transactionManager)
                .reader(naverNewsUrlReader)
                .writer(newsUrlWriter)
                .build();
    }
}