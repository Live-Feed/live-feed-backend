package com.livefeed.livefeedcrawler.batch;

import com.livefeed.livefeedcommon.kafka.record.UrlTopicKey;
import com.livefeed.livefeedcrawler.batch.configuration.BatchTestConfiguration;
import com.livefeed.livefeedcrawler.batch.configuration.CustomBatchConfiguration;
import com.livefeed.livefeedcrawler.batch.configuration.NaverNewsCrawlJobConfiguration;
import com.livefeed.livefeedcrawler.batch.reader.NaverNewsItemReader;
import com.livefeed.livefeedcrawler.batch.writer.NewsItemWriter;
import com.livefeed.livefeedcrawler.common.Page;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9094", "port=9094" })
@SpringBootTest(classes = {NaverNewsItemReader.class, NewsItemWriter.class, NaverNewsCrawlJobConfiguration.class,
        BatchTestConfiguration.class, CustomBatchConfiguration.class})
public class NaverNewsCrawlJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    @Autowired
    private NaverNewsItemReader reader;

    @Autowired
    private NewsItemWriter writer;

    @Test
    @Disabled
    @DisplayName("네이버 뉴스 크롤링 Job이 정상 실행된다.")
    void runNaverNewsCrawlJob() throws Exception {
        // given
        JobParameters jobParameters = createJobParameters();

        // when
        JobExecution jobExecution = jobLauncher.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    @Disabled
    @DisplayName("네이버 뉴스 크롤링 Step이 정상 실행된다.")
    void runGoogleNewsCrawlStep() {
        // given
        JobParameters jobParameters = createJobParameters();

        // when
        JobExecution jobExecution = jobLauncher.launchStep("naverNewsCrawlStep", jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    @Disabled
    @DisplayName("네이버 뉴스 페이지의 기사 url을 크롤링하는 ItemReader가 정상 실행된다.")
    void readGoogleNewsItem() throws Exception {
        // given
        JobParameters jobParameters = createJobParameters();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);

        // when
        String articleUrl = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            reader.open(stepExecution.getExecutionContext());
            String item = reader.read();
            reader.close();
            return item;
        });

        // then
        assertThat(articleUrl).isNotBlank();
        assertThat(articleUrl).startsWith("https://sports.naver.com/");
    }

    @Test
    @DisplayName("기사 url을 kafka로 전송하는 ItemWriter가 정상 실행된다.")
    void writeGoogleNewsItem() throws Exception {
        // given
        JobParameters jobParameters = createJobParameters();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);

        // when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            writer.write(new Chunk<>(Page.NAVER_SPORTS_NEWS.getUrls()));
            return null;
        });
    }

    JobParameters createJobParameters() {
        Page page = Page.NAVER_SPORTS_NEWS;
        UrlTopicKey urlTopicKey = new UrlTopicKey(page.getService(), page.getPlatform(), page.getTheme());

        return new JobParametersBuilder()
                .addString("pageUrl", page.getUrls().get(0))
                .addJobParameter("urlTopicKey", urlTopicKey, UrlTopicKey.class)
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();
    }
}
