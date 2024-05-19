package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.dto.KeywordEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishKeywordEvent(KeywordEvent keywordEvent) {
        eventPublisher.publishEvent(keywordEvent);
    }
}
