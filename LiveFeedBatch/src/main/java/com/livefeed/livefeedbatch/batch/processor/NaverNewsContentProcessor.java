package com.livefeed.livefeedbatch.batch.processor;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.common.dto.wrtiervaluedto.WriterValue;
import com.livefeed.livefeedbatch.batch.processor.parser.ParserProvider;
import com.livefeed.livefeedbatch.batch.processor.redis.operations.RedisOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverNewsContentProcessor implements ItemProcessor<ItemDto, ItemDto> {

    private final ParserProvider parserProvider;
    private final RedisOperations<String, Boolean> redisOperations;

    @Override
    public ItemDto process(ItemDto itemDto) throws Exception {
        UrlInfo urlInfo = itemDto.urlInfo();
        WriterValue writerValue = (WriterValue) itemDto.itemDtoInterface().getValue();
        String url = writerValue.url();

        if (redisOperations.isDuplicate(url, true)) {
            return null;
        }
        ParseResultDto parseResultDto = parserProvider.parseWebPage(urlInfo, url);
        return new ItemDto(urlInfo, parseResultDto);
    }
}
