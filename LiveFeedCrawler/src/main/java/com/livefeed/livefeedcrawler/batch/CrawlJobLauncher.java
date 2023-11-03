package com.livefeed.livefeedcrawler.batch;

import com.livefeed.livefeedcommon.kafka.record.UrlTopicKey;
import com.livefeed.livefeedcrawler.common.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlJobLauncher {

    private final JobLauncher jobLauncher;
    private final Job googleNewsCrawlJob;
    private final Job naverNewsCrawlJob;

    private void runCrawlJob(Job crawlJob, String pageUrl, UrlTopicKey urlTopicKey) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("pageUrl", pageUrl)
                .addJobParameter("urlTopicKey", urlTopicKey, UrlTopicKey.class)
                .addLocalDateTime("date", LocalDateTime.now())
                .toJobParameters();

        try {
            jobLauncher.run(crawlJob, jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void runGoogleSportsNewsCrawlJob() {
        Page page = Page.GOOGLE_SPORTS_NEWS;
        UrlTopicKey urlTopicKey = new UrlTopicKey(page.getService(), page.getPlatform(), page.getTheme());

        for (String pageUrl : page.getUrls()) {
            runCrawlJob(googleNewsCrawlJob, pageUrl, urlTopicKey);
        }
    }

    public void runNaverSportsNewsCrawlJob() {
        Page page = Page.NAVER_SPORTS_NEWS;
        UrlTopicKey urlTopicKey = new UrlTopicKey(page.getService(), page.getPlatform(), page.getTheme());

        for (String pageUrl : page.getUrls()) {
            runCrawlJob(naverNewsCrawlJob, pageUrl, urlTopicKey);
        }
    }
}
