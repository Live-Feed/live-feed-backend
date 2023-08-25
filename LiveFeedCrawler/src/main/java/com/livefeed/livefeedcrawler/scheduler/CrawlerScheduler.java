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

    @Scheduled(fixedRate = 30000)
    public void crawlGoogleNews() {
        crawlJobLauncher.runGoogleNewsCrawlJob();
    }
}
