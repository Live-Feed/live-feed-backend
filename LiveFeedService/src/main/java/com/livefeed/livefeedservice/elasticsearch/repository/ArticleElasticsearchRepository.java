package com.livefeed.livefeedservice.elasticsearch.repository;

import com.livefeed.livefeedsaver.elasticsearch.entity.ElasticsearchArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleElasticsearchRepository extends ElasticsearchRepository<ElasticsearchArticle, Long> {
}
