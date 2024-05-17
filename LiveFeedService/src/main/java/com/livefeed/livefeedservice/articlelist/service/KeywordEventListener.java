package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.dto.KeywordEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KeywordEventListener {

    @EventListener
    @Async
    public void processKeywordEvent(KeywordEvent keywordEvent) {
        log.info("keywordEvent = {}", keywordEvent);

    }
}
