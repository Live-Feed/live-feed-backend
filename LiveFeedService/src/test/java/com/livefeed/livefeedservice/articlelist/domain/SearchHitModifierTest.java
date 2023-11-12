package com.livefeed.livefeedservice.articlelist.domain;

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

    @DisplayName("explain을 통해 타입과 단어를 찾을 수 있습니다.")
    @Test
    void extractTypeAndWord() {
        // given
        String explain = "weight(content:elasticsearch in 0) [PerFieldSimilarity], result of:";
        // when
        String searchedWord = searchHitModifier.extractSearchedWord(explain);
        // then
        assertThat(searchedWord).isEqualTo("elasticsearch");
    }

    @Test
    @DisplayName("search 한 쿼리들의 단어를 볼드 형식으로 변경하는지 확인하는 테스트")
    void addBold() {
        // given
//        String header = "<header>기사 제목</header>";
        String body = "기사 본문 테스트 elasticsearch data elasticsearch";
//        String explain = "weight(content:elasticsearch in 0) [PerFieldSimilarity], result of:";
        String word = "elasticsearch";
        // when
        String result = searchHitModifier.boldingWord(word, body);
        // then
        assertThat(result).isEqualTo("기사 본문 테스트 <b>elasticsearch</b> data <b>elasticsearch</b>");
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

    @DisplayName("<b> 가 없고 200글자보다 작은 경우 경우 앞에서부터 전부 반환합니다.")
    @Test
    void noBoldUnder100() {
        // given
        String text = "asdfwersdfqwerasdfwersdfqwerasdfwersdfqwerasdfwersdfqwerasdfwersdfqwer";
        // when
        String result = searchHitModifier.cutTextFromBodyText(text);
        // then
        assertThat(result).isEqualTo(text);
    }

    @DisplayName("<b> 가 없고 200글자보다 큰 경우 앞에서부터 200글자만 반환합니다.")
    @Test
    void NoBoldOver100() {
        // given
        String text = "asdfwersdfqwerasdfwersdfqwerasdfwersdfqwerasdfwersdfqwerasdfwersdfqwerasdfwersdfqwerasdfwersdfqwerasdfwersdfqwerasd";
        // when
        String result = searchHitModifier.cutTextFromBodyText(text);
        // then
        assertThat(result).isEqualTo(text);
    }

    @Test
    @DisplayName("키워드와 텍스트를 입력받아 해당 키워드를 bold 처리해줍니다.")
    void boldingWord() {
        // given
        String text = "이것은 특정 문자열입니다. 특정 문자열의 다른 예시도 특정 문자열입니다.";
        String keyword = "특정";
        // when
        String result = searchHitModifier.boldingWord(keyword, text);
        // then
        assertThat(result).isEqualTo("이것은 <b>특정</b> 문자열입니다. <b>특정</b> 문자열의 다른 예시도 <b>특정</b> 문자열입니다.");
    }

}
