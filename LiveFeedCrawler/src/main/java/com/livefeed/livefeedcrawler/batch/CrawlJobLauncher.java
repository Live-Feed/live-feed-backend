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

    private void runCrawlJob(Job crawlJob, String pageUrl, NewsPage.Platform platform, NewsPage.Theme theme) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("pageUrl", pageUrl)
                .addString("platform", platform.name())
                .addString("theme", theme.name())
                .addDate("date", Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .toJobParameters();

        try {
            jobLauncher.run(crawlJob, jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void runGoogleSportsNewsCrawlJob() {
        for (String pageUrl : NewsPage.GOOGLE_SPORTS_NEWS.getUrls()) {
            runCrawlJob(googleNewsCrawlJob, pageUrl, NewsPage.Platform.GOOGLE, NewsPage.Theme.SPORTS);
        }
    }

    public void runNaverSportsNewsCrawlJob() {
        for (String pageUrl : NewsPage.NAVER_SPORTS_NEWS.getUrls()) {
            runCrawlJob(naverNewsCrawlJob, pageUrl, NewsPage.Platform.NAVER, NewsPage.Theme.SPORTS);
        }
    }
}
