package com.livefeed.livefeedsaver.opensearch.repository;

import com.livefeed.livefeedsaver.opensearch.entity.OpensearchArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleOpensearchRepository extends ElasticsearchRepository<OpensearchArticle, String> {
}
