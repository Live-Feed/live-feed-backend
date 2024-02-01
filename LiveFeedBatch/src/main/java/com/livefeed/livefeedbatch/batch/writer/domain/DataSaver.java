package com.livefeed.livefeedbatch.batch.writer.domain;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.writer.elasticsearch.entity.ElasticsearchArticle;
import com.livefeed.livefeedbatch.batch.writer.elasticsearch.repository.ArticleElasticsearchRepository;
import com.livefeed.livefeedbatch.batch.writer.rdb.entity.Article;
import com.livefeed.livefeedbatch.batch.writer.rdb.service.RdbSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSaver {

    private final RdbSaveService rdbSaveService;
    private final ArticleElasticsearchRepository articleElasticsearchRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveArticle(UrlInfo key, ParseResultDto value) {
        Article article = rdbSaveService.saveArticle(key, value);

        log.info("saved article id = {}", article.getId());
        ElasticsearchArticle elasticsearchArticle = ElasticsearchArticle.from(article, key, value);
        articleElasticsearchRepository.save(elasticsearchArticle);
        log.info("elasticsearch data save success, article id = {}", elasticsearchArticle.getId());
    }

}
