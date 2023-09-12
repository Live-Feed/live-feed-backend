package com.livefeed.livefeedcrawler.batch;

import com.livefeed.livefeedcrawler.common.NewsPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlJobLauncher {

    private final JobLauncher jobLauncher;
    private final Job googleNewsCrawlJob;
    private final Job naverNewsCrawlJob;

    private void runCrawlJob(Job crawlJob, String pageUrl) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("pageUrl", pageUrl)
                .addDate("date", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .toJobParameters();

        try {
            jobLauncher.run(crawlJob, jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void runGoogleNewsCrawlJob() {
        for (String pageUrl : NewsPage.GOOGLE_NEWS.getUrls()) {
            runCrawlJob(googleNewsCrawlJob, pageUrl);
        }
    }

    public void runNaverNewsCrawlJob() {
        for (String pageUrl : NewsPage.NAVER_NEWS.getUrls()) {
            runCrawlJob(naverNewsCrawlJob, pageUrl);
        }
    }
}
