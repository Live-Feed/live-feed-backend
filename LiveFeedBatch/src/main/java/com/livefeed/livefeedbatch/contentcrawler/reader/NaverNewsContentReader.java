package com.livefeed.livefeedbatch.contentcrawler.reader;

import com.livefeed.livefeedbatch.contentcrawler.parser.ParserProvider;
import com.livefeed.livefeedbatch.contentcrawler.parser.dto.ParseResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@RequiredArgsConstructor
public class NaverNewsContentReader implements ItemReader<String> {

    private final ParserProvider parserProvider;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        // TODO: 12/15/23 희원 : reader에서 기존 url 크롤링한 결과인 urlInfo와 url 값이 필요합니다. 이후 아래 메서드의 결과를 다음 step 으로 넘겨줍니다.
        // ParseResultDto parseResultDto = parserProvider.parseWebPage(urlInfo, url);
        return null;
    }
}
