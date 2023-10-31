package com.livefeed.livefeedservice.elasticsearchstudy.searchafter;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import com.livefeed.livefeedservice.elasticsearch.repository.ArticleElasticsearchRepository;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.MultiGetItem;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
public class SearchAfterQueryTest {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ArticleElasticsearchRepository articleElasticsearchRepository;

    @Test
    @DisplayName("search after 쿼리 테스트")
    void searchAfter() {
        // given
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(t -> t.field("title").query("search")))
//                .withSort(Sort.by(Sort.Order.desc("title")))
                .build();

//        Query matchQuery = QueryBuilders.match(builder -> builder.field("title").query("search"));
//        NativeQuery nativeQuery = NativeQuery.builder()
//                .withQuery(matchQuery)
//                .withSort(Sort.by("title"))
//                .build();

//        SearchHitsIterator<IndexName> search = elasticsearchOperations.searchForStream(nativeQuery, IndexName.class);
        SearchHits<IndexName> search = elasticsearchOperations.search(query, IndexName.class);


        // when
//        List<MultiGetItem<IndexName>> multiGetItems = elasticsearchOperations.multiGet(query, IndexName.class);
//        SearchHits<IndexName> search = elasticsearchOperations.search(query, IndexName.class);
        // then
//        log.info("multiGetItems = {}", multiGetItems);
        log.info("search = {}", search);

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

    @Getter
    @Document(indexName = "index_name")
    private static class IndexName {
//        @Id
//        private String id;

        @Field(type = FieldType.Text)
        private String title;

        @Field(type = FieldType.Text)
        private String content;
    }
}
