package com.livefeed.livefeedbatch.configuration;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedbatch.batch.processor.NaverNewsContentProcessor;
import com.livefeed.livefeedbatch.batch.processor.NaverUrlEncodeProcessor;
import com.livefeed.livefeedbatch.batch.reader.NaverNewsUrlReader;
import com.livefeed.livefeedbatch.batch.writer.NewsUrlWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class NaverNewsUrlCrawlJobConfiguration {
    private static final int chunkSize = 20;

    private final NaverNewsUrlReader naverNewsUrlReader;
    private final NaverUrlEncodeProcessor naverUrlEncodeProcessor;
    private final NaverNewsContentProcessor naverNewsContentProcessor;
    private final NewsUrlWriter newsUrlWriter;

    @Bean
    public Job naverNewsCrawlJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("naverNewsCrawlJob", jobRepository)
                .start(naverNewsCrawlStep(jobRepository, transactionManager))
                .build();
    }

    public Step naverNewsCrawlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("naverNewsUrlCrawlStep", jobRepository)
                .<String, ItemDto>chunk(chunkSize, transactionManager)
                .reader(naverNewsUrlReader)
                .processor(createProcessor())
                .writer(newsUrlWriter)
                .build();
    }

    private CompositeItemProcessor<String, ItemDto> createProcessor() throws Exception {
        CompositeItemProcessor<String, ItemDto> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(Arrays.asList(naverUrlEncodeProcessor, naverNewsContentProcessor));
        compositeItemProcessor.afterPropertiesSet();
        return compositeItemProcessor;
    }
}