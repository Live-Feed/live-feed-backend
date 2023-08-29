package com.livefeed.livefeedcrawler.batch;

import com.livefeed.livefeedcrawler.batch.configuration.BatchTestConfiguration;
import com.livefeed.livefeedcrawler.batch.configuration.TestJobConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest(classes = {TestJobConfiguration.class, BatchTestConfiguration.class})
public class SpringBatchJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    @Test
    @DisplayName("Spring Batch가 정상 실행된다.")
    void runSpringBatch() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncher.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}
