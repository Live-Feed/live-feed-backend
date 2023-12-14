package com.livefeed.livefeedbatch.batch.processor;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedbatch.batch.common.dto.ItemDtoInterface;
import com.livefeed.livefeedbatch.batch.common.dto.wrtiervaluedto.WriterValue;
import com.livefeed.livefeedbatch.batch.processor.parser.ParserProvider;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.processor.redis.operations.RedisOperations;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
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

        // TODO: 12/15/23 희원 : reader에서 기존 url 크롤링한 결과인 ItemDto 값이 필요합니다. 이후 아래 메서드의 결과를 다음 step 으로 넘겨줍니다.

//        UrlInfo urlInfo = itemDto.urlInfo();
//        WriterValue writerValue = (WriterValue) itemDto.itemDtoInterface().getValue();
//        String url = writerValue.url();

//        if (redisOperations.isDuplicate(url, true)) {
//            return null;
//        }
//
//        ParseResultDto parseResultDto = parserProvider.parseWebPage(item, url);
//
//        return new ItemDto(urlInfo, parseResultDto);

        return null;
    }
}
