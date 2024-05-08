package com.livefeed.livefeedbatch.batch.processor;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.processor.parser.ParserProvider;
import com.livefeed.livefeedbatch.batch.common.redis.operations.RedisOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class NaverNewsContentProcessor implements ItemProcessor<String, ItemDto> {

    @Value("#{jobParameters[urlInfo]}")
    private UrlInfo urlInfo;

    private final ParserProvider parserProvider;
    private final RedisOperations<String, Boolean> redisOperations;

    @Override
    public ItemDto process(String url) {
        if (redisOperations.getOrSet(url, true)) {
            return null;
        }
        ParseResultDto parseResultDto = parserProvider.parseWebPage(urlInfo, url);
        return new ItemDto(urlInfo, parseResultDto);
    }
}
