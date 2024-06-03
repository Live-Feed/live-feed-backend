package com.livefeed.livefeedservice.articlelist.domain.repository;

import com.livefeed.livefeedservice.articlelist.dto.KeywordEvent;

public interface KeywordEventPublisher {

    void publishKeywordEvent(KeywordEvent keywordEvent);
}
