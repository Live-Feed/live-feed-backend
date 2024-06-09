package com.livefeed.livefeedbatch.batch.writer.elasticsearch.repository;

import com.livefeed.livefeedbatch.batch.writer.elasticsearch.entity.ElasticsearchArticle;

import java.util.List;

public interface ArticleElasticsearchRepository {

    void saveAll(List<ElasticsearchArticle> articles);
}
