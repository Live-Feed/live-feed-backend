package com.livefeed.livefeedbatch.batch.writer.elasticsearch.repository;

import com.livefeed.livefeedbatch.batch.writer.elasticsearch.entity.ElasticsearchArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleElasticsearchRepositoryImpl implements ArticleElasticsearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void saveAll(List<ElasticsearchArticle> articles) {

        List<IndexQuery> indexQueries = articles.stream().map(article -> {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setObject(article);
            return indexQuery;
        }).toList();

        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String indexName = "sports_articles" + "_" + datePrefix;

        elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of(indexName));
    }
}
