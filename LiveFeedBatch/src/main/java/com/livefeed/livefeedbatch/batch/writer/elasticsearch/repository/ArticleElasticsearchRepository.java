package com.livefeed.livefeedbatch.batch.writer.elasticsearch.repository;

import com.livefeed.livefeedbatch.batch.writer.elasticsearch.entity.ElasticsearchArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleElasticsearchRepository extends ElasticsearchRepository<ElasticsearchArticle, Long> {
}
