package com.livefeed.livefeedcrawler.batch;

import com.livefeed.livefeedcrawler.batch.configuration.BatchTestConfiguration;
import com.livefeed.livefeedcrawler.batch.configuration.GoogleNewsCrawlJobConfiguration;
import com.livefeed.livefeedcrawler.batch.reader.GoogleNewsItemReader;
import com.livefeed.livefeedcrawler.batch.writer.NewsItemWriter;
import com.livefeed.livefeedcrawler.common.NewsPage;
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
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9094", "port=9094" })
@SpringBootTest(classes = {GoogleNewsItemReader.class, NewsItemWriter.class, GoogleNewsCrawlJobConfiguration.class, BatchTestConfiguration.class})
public class GoogleNewsCrawlJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncher;

    @Autowired
    private GoogleNewsItemReader reader;

    @Autowired
    private NewsItemWriter writer;

    @Test
    @DisplayName("구글 뉴스 크롤링 Job이 정상 실행된다.")
    void runGoogleNewsCrawlJob() throws Exception {
        // given
        JobParameters jobParameters = createJobParameters();

        // when
        JobExecution jobExecution = jobLauncher.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    @DisplayName("구글 뉴스 크롤링 Step이 정상 실행된다.")
    void runGoogleNewsCrawlStep() {
        // given
        JobParameters jobParameters = createJobParameters();

        // when
        JobExecution jobExecution = jobLauncher.launchStep("googleNewsCrawlStep", jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    @DisplayName("구글 뉴스 페이지의 기사 url을 크롤링하는 ItemReader가 정상 실행된다.")
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
        assertThat(articleUrl).startsWith("https://news.google.com/articles/");
    }

    @Test
    @DisplayName("기사 url을 kafka로 전송하는 ItemWriter가 정상 실행된다.")
    void writeGoogleNewsItem() {
        writer.write(new Chunk<>(NewsPage.GOOGLE_NEWS.getUrls()));
    }

    JobParameters createJobParameters() {
        return new JobParametersBuilder()
                .addString("pageUrl", NewsPage.GOOGLE_NEWS.getUrls().get(0))
                .addDate("date", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .toJobParameters();
    }
}
