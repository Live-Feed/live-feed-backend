package com.livefeed.livefeedcrawler.scheduler;

import com.livefeed.livefeedcrawler.batch.CrawlJobLauncher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CrawlerScheduler {

    private final CrawlJobLauncher crawlJobLauncher;

    public void crawlGoogleNews() {
        crawlJobLauncher.runGoogleNewsCrawlJob();
    }

    @Scheduled(fixedRate = 300000)
    public void crawlNaverNews() {
        crawlJobLauncher.runNaverNewsCrawlJob();
    }
}
