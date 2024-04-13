package com.livefeed.livefeedservice.common.util;

import com.livefeed.livefeedservice.articlelist.util.SearchQueryMaker;
import com.livefeed.livefeedservice.articlelist.util.SearchQueryParam;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;


@Disabled
@Slf4j
@SpringBootTest
class SearchQueryMakerTest {

    @Autowired
    private SearchQueryMaker searchQueryMaker;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @DisplayName("정상적으로 목록을 가져오는지 확인합니다.")
    @Test
    void searchQuery() {
        // given
        List<String> types = List.of("title", "content");
        List<String> keywords = List.of("data", "analyse");
        SearchQueryParam queryParam = SearchQueryParam.makeParam(types, keywords, 5, List.of("id-desc"), null, null);
        // when
        NativeQuery query = searchQueryMaker.makeArticleListQuery(queryParam);
        SearchHits<IndexName> searchHits = elasticsearchOperations.search(query, IndexName.class);
        // then
        for (SearchHit<SearchQueryMakerTest.IndexName> searchHit : searchHits) {
            log.info("search info = {}", searchHit.getSortValues());
            log.info("search = {}", searchHit.getContent());
        }

    }

    @ToString
    @Getter
    @Document(indexName = "index_name")
    private static class IndexName {
        @Id
        private Long id;

        @Field(type = FieldType.Text)
        private String title;

        @Field(type = FieldType.Text)
        private String content;
    }

}