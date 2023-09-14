package com.livefeed.livefeedcrawler.common;

public record NewsKey(
        String platform,
        String theme
) {

    public static NewsKey of(String platform, String theme) {
        return new NewsKey(platform, theme);
    }
}
