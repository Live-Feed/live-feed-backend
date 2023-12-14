package com.livefeed.livefeedbatch.contentcrawler.parser.parsermanager;

import com.livefeed.livefeedbatch.contentcrawler.parser.dto.ParseResultDto;
import com.livefeed.livefeedbatch.urlcrawler.dto.UrlInfo;

public interface ParserManager {

    ParseResultDto parseWebPage(UrlInfo keyDto, String url);
}
