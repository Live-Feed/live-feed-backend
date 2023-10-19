package com.livefeed.livefeedparser.parser.parsermanager;

import com.livefeed.livefeedcommon.kafka.record.UrlTopicKey;
import com.livefeed.livefeedparser.parser.dto.ParseResultDto;

public interface ParserManager {

    ParseResultDto parseWebPage(UrlTopicKey keyDto, String url);
}
