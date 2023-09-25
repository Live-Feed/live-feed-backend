package com.livefeed.livefeedparser.parser.parsermanager;

import com.livefeed.livefeedparser.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedparser.parser.dto.ParseResultDto;

public interface ParserManager {

    ParseResultDto parseWebPage(ConsumerKeyDto keyDto, String url);
}
