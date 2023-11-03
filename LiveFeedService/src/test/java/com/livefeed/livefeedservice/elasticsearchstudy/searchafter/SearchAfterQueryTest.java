package com.livefeed.livefeedservice.elasticsearchstudy.searchafter;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptionsBuilders;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.OpenPointInTimeResponse;
import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import com.livefeed.livefeedservice.elasticsearch.repository.ArticleElasticsearchRepository;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
public class SearchAfterQueryTest {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ArticleElasticsearchRepository articleElasticsearchRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @DisplayName("동적으로 제목이나 내용에 대해 전달받은 키워드를 서치하는 쿼리 생성")
    @Test
    void dynamicQuery() {
        // given
        List<String> types = List.of("title", "content");
        List<String> keywords = List.of("data", "analyse");
        // when
        NativeQueryBuilder builder = NativeQuery.builder();

        List<Query> queryList = new ArrayList<>();

        for (String type : types) {
            for (String keyword : keywords) {
                Query query = Query.of(q -> q.match(t -> t.field(type).query(keyword)));
                queryList.add(query);
            }
        }

        NativeQuery query = builder
                .withQuery(Query.of(b -> b.bool(bool -> bool.should(queryList))))
                .build();

        SearchHits<IndexName> searchHits = elasticsearchOperations.search(query, IndexName.class);
        // then
        for (SearchHit<IndexName> searchHit : searchHits) {
            log.info("search info = {}", searchHit.getSortValues());
            log.info("search = {}", searchHit.getContent());
        }
    }

    @DisplayName("point in time 아이디 생성")
    @Test
    void makePIT() throws IOException {
        // given
        OpenPointInTimeResponse pit = elasticsearchClient.openPointInTime(b -> b.index("index_name")
                .keepAlive(Time.of(builder -> builder.time("1m"))));

        // then
        String id = pit.id();
        log.info("pid id = {}", id);
    }

    @Test
    @DisplayName("search after 쿼리 테스트")
    void searchAfter() {
        // given
        String pitId = "3eaGBAEKaW5kZXhfbmFtZRZJVFdJS0x0TVE5LUtWTDcwV1drdWlRABZaaXhVWnNla1JjYVgwOFJDcTZoV1dRAAAAAAAAAIekFm5Kc3EycHpuU2NhYkJncWZzUlVtV1EAARZJVFdJS0x0TVE5LUtWTDcwV1drdWlRAAA=";
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(t -> t.field("title").query("data")))
                .withSort(SortOptionsBuilders.field(builder -> builder.field("id").order(SortOrder.Desc)))
                .withSearchAfter(List.of(34))
                .withMaxResults(3)
//                .withPointInTime(new Query.PointInTime(pitId, Duration.ofMinutes(1)))
                .build();

        SearchHits<IndexName> search = elasticsearchOperations.search(query, IndexName.class);
        List<SearchHit<IndexName>> searchHits = search.getSearchHits();

        for (SearchHit<IndexName> searchHit : searchHits) {
            log.info("search info = {}", searchHit.getSortValues());
            log.info("search = {}", searchHit.getContent());
        }
    }

    @Test
    @DisplayName("신문기사 저장 테스트")
    void saveArticle() {
        // given
        ElasticsearchArticle article = ElasticsearchArticle.builder()
                .id(1L)
                .articleTitle("테스트")
                .build();

        // when
        articleElasticsearchRepository.save(article);
        // then
//        Optional<ElasticsearchArticle> result = articleElasticsearchRepository.findByArticleTitle("테스트");
        Optional<ElasticsearchArticle> result = articleElasticsearchRepository.findById(1L);
        log.info("result = {}", result.get());
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
