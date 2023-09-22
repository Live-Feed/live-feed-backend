package com.livefeed.livefeedparser.parser.parsermanager;

import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedparser.parser.parser.Parser;
import com.livefeed.livefeedparser.parser.dto.ParseResultDto;
import com.livefeed.livefeedparser.parser.exception.NoMatchingParserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleParserManager implements ParserManager {

    private final List<Parser> parserList;

    @Override
    public ParseResultDto parseWebPage(ConsumerKeyDto key, String url) {
        Parser parser = parserList.stream().filter(p -> p.support(key))
                .findFirst().orElseThrow(() -> new NoMatchingParserException("해당하는 파서가 존재하지 않습니다."));

        return parser.parseWebPage(url);
    }
}
