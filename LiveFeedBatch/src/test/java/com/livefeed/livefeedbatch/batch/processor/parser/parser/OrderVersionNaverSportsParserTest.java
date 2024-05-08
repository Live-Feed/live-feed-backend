package com.livefeed.livefeedbatch.batch.processor.parser.parser;

import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class OrderVersionNaverSportsParserTest {


    @Test
    @DisplayName("")
    void parse() {
        // given
        NaverSportsParser naverSportsParser = new NaverSportsParser(new NameAndEmailParser());
        // when
        ParseResultDto result = naverSportsParser.parseWebPage("https://m.sports.naver.com/general/article/052/0002029306");
        // then
        log.info("result = {}", result);
    }

}