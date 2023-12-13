package com.livefeed.livefeedbatch.contentcrawler.dto;

public record BodyDto(
        String html,
        String journalistName,
        String journalistEmail
) {

    public static BodyDto of(String html, String journalistName, String journalistEmail) {
        return new BodyDto(html, journalistName, journalistEmail);
    }
}
