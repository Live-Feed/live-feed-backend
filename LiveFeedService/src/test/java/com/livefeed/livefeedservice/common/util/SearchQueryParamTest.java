package com.livefeed.livefeedservice.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SearchQueryParamTest {

    @DisplayName("요청 파라미터로 받은 정보들을 elasticsearch에서 사용할 쿼리에 파라미터 dto로 변환하는 테스트")
    @Test
    void makeSearchQueryParam() {
        // given
        List<String> keywords = List.of("key1", "key2");
        List<String> type = List.of("type1");
        int size = 5;
        List<String> sorts = List.of("id-desc", "title-asc");
        Long lastId = null;
        String pit = null;
        // when
        SearchQueryParam result = SearchQueryParam.makeParam(keywords, type, size, sorts, lastId, pit);
        // then
        assertThat(result.sort()).isEqualTo(Sort.by(Sort.Order.desc("id"), Sort.Order.asc("title")));
        assertThat(result.sort()).isNotEqualTo(Sort.by(Sort.Order.asc("title"), Sort.Order.desc("id")));
    }

    @DisplayName("요청 파라미터로 받은 정보들 중에 정렬 파라미터의 값이 asc, desc 값이 아니면 에러를 발생시킨다.")
    @Test
    void notAllowParam() {
        // given
        List<String> keywords = List.of("key1", "key2");
        List<String> type = List.of("type1");
        int size = 5;
        List<String> sorts = List.of("id-descc", "title-ascc");
        Long lastId = null;
        String pit = null;
        // when
        // then
        assertThatThrownBy(() -> SearchQueryParam.makeParam(keywords, type, size, sorts, lastId, pit))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("올바르지 않은 정렬 정보입니다.");
    }
}