package com.livefeed.livefeedcrawler.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum NewsPage {
    GOOGLE_NEWS(List.of(GoogleNewsPage.SPORTS.url)),
    NAVER_NEWS(Stream.of(NaverNewsPage.SPORTS.urls)
            .flatMap(List::stream)
            .collect(Collectors.toList()));

    private final List<String> urls;

    @RequiredArgsConstructor
    private enum GoogleNewsPage {
        SPORTS("https://news.google.com/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRFp1ZEdvU0FtdHZHZ0pMVWlnQVAB?hl=ko&gl=KR&ceid=KR%3Ako");

        private final String url;
    }

    @RequiredArgsConstructor
    private enum NaverNewsPage {
        SPORTS(List.of(NaverSportsNewsPage.KBASEBALL.url, NaverSportsNewsPage.WBASEBALL.url,
                NaverSportsNewsPage.KFOOTBALL.url, NaverSportsNewsPage.WFOOTBALL.url,
                NaverSportsNewsPage.BASKETBALL.url, NaverSportsNewsPage.VOLLEYBALL.url,
                NaverSportsNewsPage.GOLF.url, NaverSportsNewsPage.GENERAL.url));

        private final List<String> urls;

        @RequiredArgsConstructor
        private enum NaverSportsNewsPage {
            KBASEBALL("https://sports.naver.com/kbaseball/news/index"),
            WBASEBALL("https://sports.naver.com/wbaseball/news/index"),
            KFOOTBALL("https://sports.naver.com/kfootball/news/index"),
            WFOOTBALL("https://sports.naver.com/wfootball/news/index"),
            BASKETBALL("https://sports.naver.com/basketball/news/index"),
            VOLLEYBALL("https://sports.naver.com/volleyball/news/index"),
            GOLF("https://sports.naver.com/golf/news/index"),
            GENERAL("https://sports.naver.com/general/news/index");

            private final String url;
        }
    }
}
