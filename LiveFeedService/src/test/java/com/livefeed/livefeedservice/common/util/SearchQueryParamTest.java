package com.livefeed.livefeedservice.common.util;

import com.livefeed.livefeedservice.articlelist.util.SearchQueryParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SearchQueryParamTest {

    @DisplayName("요청 파라미터로 받은 정보들을 elasticsearch에서 사용할 쿼리에 파라미터 dto로 변환하는 테스트")
    @Test
    void makeRelatedSearchQueryParam() {
        // given
        List<String> keywords = List.of("key1", "key2");
        List<String> type = List.of("type1");
        int size = 5;
        Long lastId = null;
        String pit = null;
        // when
        SearchQueryParam result = SearchQueryParam.makeParam(type, keywords, size, 12.5, lastId, pit, true);
        // then
        assertThat(result.getSort()).isEqualTo(Sort.by(Sort.Order.desc("_score"), Sort.Order.desc("id")));
    }

    @DisplayName("요청 파라미터로 받은 정보들을 elasticsearch에서 사용할 쿼리에 파라미터 dto로 변환하는 테스트")
    @Test
    void makeNotRelatedSearchQueryParam() {
        // given
        List<String> keywords = List.of("key1", "key2");
        List<String> type = List.of("type1");
        int size = 5;
        Long lastId = null;
        String pit = null;
        // when
        SearchQueryParam result = SearchQueryParam.makeParam(type, keywords, size, 12.5, lastId, pit, false);
        // then
        assertThat(result.getSort()).isEqualTo(Sort.by(Sort.Order.desc("id")));
    }
}