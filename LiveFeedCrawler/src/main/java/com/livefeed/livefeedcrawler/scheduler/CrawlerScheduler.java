package com.livefeed.livefeedcrawler.scheduler;

import com.livefeed.livefeedcrawler.crawler.NewsCrawlerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CrawlerScheduler {

    private final NewsCrawlerTemplate newsCrawler;

    @Scheduled(fixedRate = 300000)
    public void crawlPage() {
        newsCrawler.crawlPage();
    }
}
