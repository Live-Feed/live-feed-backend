package com.livefeed.livefeedviewsbatch.batch.writer;

import com.livefeed.livefeedviewsbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedviewsbatch.batch.writer.rdb.service.RdbSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewsWriter implements ItemWriter<ItemDto> {

    private final RdbSaveService rdbSaveService;

    @Override
    public void write(Chunk<? extends ItemDto> chunk) {
        List<? extends ItemDto> items = chunk.getItems();

        for (ItemDto item : items) {
            try {
                rdbSaveService.setViews(item.key(), item.value());
                log.info("update views, key: {}, value: {}", item.key(), item.value());
            } catch (Exception e) {
                log.error("DB Exception Occur message = {} and Error Article Id = {}", e.getMessage(), item.key());
            }
        }
    }
}