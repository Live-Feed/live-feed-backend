package com.livefeed.livefeedservice.articlelist.domain;

import com.livefeed.livefeedservice.articlelist.infra.SearchHitModifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SearchHitModifierTest {

    private final SearchHitModifier searchHitModifier = new SearchHitModifier();

    @Test
    @DisplayName("html 태그들을 제외한 순수 text 만 추출하는지 확인하는 테스트")
    void extractText() {
        // given
        String html = "<div><div>기사 본문</div><span>테스트</span></div>";
        // when
        String text = searchHitModifier.extractTextFromHtml(html);
        // then
        assertThat(text).isEqualTo("기사 본문 테스트");
    }

    @DisplayName("기사 본문에서 첫번째 이미지 url 을 추출하는 확인하는 테스트")
    @Test
    void extractImgUrl() {
        // given
        String html = "<div><img src=https://imgnews.pstatic.net/image/311/2023/10/31/0001655895_001_20231031173201332.jpg?type=w647 alt=>" +
                "<div>기사 본문</div><span>테스트</span></div>";
        // when
        String result = searchHitModifier.extractFirstImgFromHtml(html);
        // then
        assertThat(result).isEqualTo("https://imgnews.pstatic.net/image/311/2023/10/31/0001655895_001_20231031173201332.jpg?type=w647");
    }
}
