package com.livefeed.livefeedbatch.batch.processor.parser.parsermanager;

import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;

public interface ParserManager {

    ParseResultDto parseWebPage(UrlInfo keyDto, String url);
}
