package com.livefeed.livefeedcrawler.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum NewsPage {
    GOOGLE_NEWS(List.of(GoogleNewsPage.SPORTS.url));

    private final List<String> urls;

    @RequiredArgsConstructor
    private enum GoogleNewsPage {
        SPORTS("https://news.google.com/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRFp1ZEdvU0FtdHZHZ0pMVWlnQVAB?hl=ko&gl=KR&ceid=KR%3Ako");

        private final String url;
    }
}
