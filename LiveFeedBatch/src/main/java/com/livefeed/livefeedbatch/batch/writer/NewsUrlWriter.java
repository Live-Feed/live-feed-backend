package com.livefeed.livefeedbatch.batch.writer;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.writer.elasticsearch.entity.ElasticsearchArticle;
import com.livefeed.livefeedbatch.batch.writer.elasticsearch.repository.ArticleElasticsearchRepository;
import com.livefeed.livefeedbatch.batch.writer.rdb.entity.Article;
import com.livefeed.livefeedbatch.batch.writer.rdb.service.RdbSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsUrlWriter implements ItemWriter<ItemDto> {

    private final RdbSaveService rdbSaveService;
    private final ArticleElasticsearchRepository articleElasticsearchRepository;

    @Override
    public void write(Chunk<? extends ItemDto> chunk) throws Exception {
        List<? extends ItemDto> items = chunk.getItems();

        for (ItemDto item : items) {
            UrlInfo key = item.urlInfo();
            ParseResultDto value = (ParseResultDto) item.itemDtoInterface().getValue();
            log.info("kafka consumerRecord key = {}, articleValue = {}", key, value);
            Article article = rdbSaveService.saveArticle(key, value);

            ElasticsearchArticle elasticsearchArticle = ElasticsearchArticle.from(article, key, value);
            articleElasticsearchRepository.save(elasticsearchArticle);
        }
    }
}