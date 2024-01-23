package com.livefeed.livefeedbatch.batch.writer;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.writer.domain.DataSaver;
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

    private final DataSaver dataSaver;

    @Override
    public void write(Chunk<? extends ItemDto> chunk) {
        List<? extends ItemDto> items = chunk.getItems();

        for (ItemDto item : items) {
            UrlInfo key = item.urlInfo();
            ParseResultDto value = (ParseResultDto) item.itemDtoInterface().getValue();
            try {
                dataSaver.saveArticle(key, value);
            } catch (Exception e) {
                log.error("DB Exception Occur message = {} and Error Article Title = {}", e.getMessage(), value.url());
            }
        }
    }
}