package com.livefeed.livefeedbatch.batch.writer;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.domain.entity.Article;
import com.livefeed.livefeedbatch.batch.util.NewArticleBucket;
import com.livefeed.livefeedbatch.batch.writer.domain.DataSaver;
import com.livefeed.livefeedbatch.batch.writer.elasticsearch.entity.ElasticsearchArticle;
import com.livefeed.livefeedbatch.batch.writer.elasticsearch.repository.ArticleElasticsearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsWriter implements ItemWriter<ItemDto> {

    private final DataSaver dataSaver;
    private final ArticleElasticsearchRepository articleElasticsearchRepository;

    @Override
    public void write(Chunk<? extends ItemDto> chunk) {
        List<? extends ItemDto> items = chunk.getItems();
        List<ElasticsearchArticle> successItem = new ArrayList<>();

        for (ItemDto item : items) {
            UrlInfo key = item.urlInfo();
            ParseResultDto value = (ParseResultDto) item.itemDtoInterface().getValue();
            try {
                Article article = dataSaver.saveArticle(key, value);
                ElasticsearchArticle elasticsearchArticle = ElasticsearchArticle.from(article, key, value);
                successItem.add(elasticsearchArticle);
                NewArticleBucket.add(article.getId());
            } catch (Exception e) {
                log.error("DB Exception Occur message = {} and Error Article Title = {}", e.getMessage(), value.url());
            }
        }
        articleElasticsearchRepository.saveAll(successItem);
        log.info("elasticsearch data save success, article count = {}", successItem.size());
    }
}