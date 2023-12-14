package com.livefeed.livefeedbatch.contentcrawler.parser.parsermanager;

import com.livefeed.livefeedbatch.contentcrawler.parser.dto.ParseResultDto;
import com.livefeed.livefeedbatch.contentcrawler.parser.exception.NoMatchingParserException;
import com.livefeed.livefeedbatch.contentcrawler.parser.parser.Parser;
import com.livefeed.livefeedbatch.urlcrawler.dto.UrlInfo;
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
    public ParseResultDto parseWebPage(UrlInfo key, String url) {
        Parser parser = parserList.stream().filter(p -> p.support(key))
                .findFirst().orElseThrow(() -> new NoMatchingParserException("해당하는 파서가 존재하지 않습니다."));

        return parser.parseWebPage(url);
    }
}
