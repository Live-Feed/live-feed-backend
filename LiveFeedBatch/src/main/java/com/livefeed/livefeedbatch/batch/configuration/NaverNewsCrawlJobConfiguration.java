package com.livefeed.livefeedbatch.batch.configuration;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedbatch.batch.processor.NaverNewsContentProcessor;
import com.livefeed.livefeedbatch.batch.reader.NaverNewsUrlReader;
import com.livefeed.livefeedbatch.batch.writer.NewsWriter;
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

    private final NaverNewsUrlReader naverNewsUrlReader;
    private final NaverNewsContentProcessor naverNewsContentProcessor;
    private final NewsWriter newsWriter;

    @Bean
    public Job naverNewsCrawlJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("naverNewsCrawlJob", jobRepository)
                .start(naverNewsCrawlStep(jobRepository, transactionManager))
                .build();
    }

    public Step naverNewsCrawlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("naverNewsUrlCrawlStep", jobRepository)
                .<String, ItemDto>chunk(chunkSize, transactionManager)
                .reader(naverNewsUrlReader)
                .processor(naverNewsContentProcessor)
                .writer(newsWriter)
                .build();
    }
}