package com.livefeed.livefeedparser.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;

@Getter
@RequiredArgsConstructor
public enum ArticleTheme {

    // 스포츠, IT/과학, 정치
    SPORTS(Header.SPORTS), IT_SCIENCE(Header.TEMP), POLITICS(Header.TEMP);

    private final Header header;


    public static By getSrcValue(String source) {
        int tagIndex = source.indexOf('.');

        if (source.substring(0, tagIndex).equals("class")) {
            return By.className(source.substring(tagIndex + 1));
        }
        return null;
    }
    
    @Getter
    @RequiredArgsConstructor
    public enum Header {
        SPORTS("class.news_headline", "class.title", "class.info",
                "class.info.bar", "class.info.href", null),

        TEMP(null, null, null,
                null, null, null);

        private final String outerHtml;
        private final String articleTitle;
        private final String createdAt;
        private final String publicationTime;
        private final String originArticleUrl;
        private final String journalistName;
    }


}
