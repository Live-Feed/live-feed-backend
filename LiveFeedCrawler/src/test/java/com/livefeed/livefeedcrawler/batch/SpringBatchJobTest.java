package com.livefeed.livefeedcrawler.batch;

import com.livefeed.livefeedcommon.kafka.record.UrlTopicKey;
import com.livefeed.livefeedcrawler.batch.configuration.BatchTestConfiguration;
import com.livefeed.livefeedcrawler.batch.configuration.CustomBatchConfiguration;
import com.livefeed.livefeedcrawler.batch.configuration.TestJobConfiguration;
import com.livefeed.livefeedcrawler.common.Page;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest(classes = {TestJobConfiguration.class, BatchTestConfiguration.class, CustomBatchConfiguration.class})
public class SpringBatchJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    @Test
    @DisplayName("Spring Batch가 정상 실행된다.")
    void runSpringBatch() throws Exception {
        // given
        Page page = Page.NAVER_SPORTS_NEWS;
        UrlTopicKey urlTopicKey = new UrlTopicKey(page.getService(), page.getPlatform(), page.getTheme());

        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("date", LocalDateTime.now())
                .addJobParameter("urlTopicKey", urlTopicKey, UrlTopicKey.class)
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncher.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}
