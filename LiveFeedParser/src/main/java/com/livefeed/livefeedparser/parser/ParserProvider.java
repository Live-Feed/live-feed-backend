package com.livefeed.livefeedparser.parser;

import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedparser.parser.dto.ParseResultDto;
import com.livefeed.livefeedparser.parser.parsermanager.ParserManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 이후에 블로그를 파싱하고 싶다면 ParserManager를 구현한
 * BlogParserManager를 구현하고 ParserManager 빈들을 리스트로 받아
 * 그 중 해당하는 ParserManager를 선택하도록 코드를 추가하면 된다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ParserProvider {

    private final ParserManager parserManager;

    public ParseResultDto parseWebPage(ConsumerKeyDto key, String url) {
        return parserManager.parseWebPage(key, url);
    }
}
