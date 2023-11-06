package com.livefeed.livefeedcrawler.common;

import com.livefeed.livefeedcommon.kafka.dto.Platform;
import com.livefeed.livefeedcommon.kafka.dto.Service;
import com.livefeed.livefeedcommon.kafka.dto.Theme;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Page {

    GOOGLE_SPORTS_NEWS(Service.ARTICLE, Platform.GOOGLE, Theme.SPORTS, List.of(GoogleNewsPage.SPORTS.url)),
    NAVER_SPORTS_NEWS(Service.ARTICLE, Platform.NAVER, Theme.SPORTS, NaverNewsPage.SPORTS.urls);

    private final Service service;
    private final Platform platform;
    private final Theme theme;
    private final List<String> urls;

    @RequiredArgsConstructor
    private enum GoogleNewsPage {
        SPORTS("https://news.google.com/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRFp1ZEdvU0FtdHZHZ0pMVWlnQVAB?hl=ko&gl=KR&ceid=KR%3Ako");

        private final String url;
    }

    @RequiredArgsConstructor
    private enum NaverNewsPage {
        SPORTS(List.of(Sports.KBASEBALL.url, Sports.WBASEBALL.url, Sports.KFOOTBALL.url, Sports.WFOOTBALL.url,
                Sports.BASKETBALL.url, Sports.VOLLEYBALL.url, Sports.GOLF.url, Sports.GENERAL.url));

        private final List<String> urls;

        @RequiredArgsConstructor
        private enum Sports {
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
