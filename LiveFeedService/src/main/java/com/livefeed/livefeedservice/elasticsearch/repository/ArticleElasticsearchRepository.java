package com.livefeed.livefeedservice.elasticsearch.repository;

import com.livefeed.livefeedservice.elasticsearch.entity.ElasticsearchArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ArticleElasticsearchRepository extends ElasticsearchRepository<ElasticsearchArticle, Long> {

    Optional<ElasticsearchArticle> findByArticleTitle(String title);
}
