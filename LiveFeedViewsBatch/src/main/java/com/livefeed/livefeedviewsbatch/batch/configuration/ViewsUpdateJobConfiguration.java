package com.livefeed.livefeedviewsbatch.batch.configuration;

import com.livefeed.livefeedviewsbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedviewsbatch.batch.reader.ViewsReader;
import com.livefeed.livefeedviewsbatch.batch.writer.ViewsWriter;
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
public class ViewsUpdateJobConfiguration {
    private static final int chunkSize = 100;

    private final ViewsReader viewsReader;
    private final ViewsWriter viewsWriter;

    @Bean
    public Job viewsUpdateJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("viewsUpdateJob", jobRepository)
                .start(viewsUpdateStep(jobRepository, transactionManager))
                .build();
    }

    public Step viewsUpdateStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("viewsUpdateStep", jobRepository)
                .<ItemDto, ItemDto>chunk(chunkSize, transactionManager)
                .reader(viewsReader)
                .writer(viewsWriter)
                .build();
    }
}