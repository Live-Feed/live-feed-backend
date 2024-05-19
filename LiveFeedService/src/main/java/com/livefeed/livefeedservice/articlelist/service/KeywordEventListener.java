package com.livefeed.livefeedservice.articlelist.service;

import com.livefeed.livefeedservice.articlelist.dto.KeywordEvent;
import com.livefeed.livefeedservice.articlelist.repository.KeywordRankRepository;
import com.livefeed.livefeedservice.newarticle.domain.Emitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordEventListener {

    private final Emitters emitters;
    private final KeywordRankRepository keywordRankRepository;

    @Async
    @EventListener
    public void processKeywordEvent(KeywordEvent keywordEvent) {
        log.info("keywordEvent = {}", keywordEvent);
        keywordRankRepository.updateKeywordScore(keywordEvent.keywords());

        Set<String> updatedTopRankKeywords = keywordRankRepository.getTopRankKeywords();
        log.info("updatedTopRankKeywords = {}", updatedTopRankKeywords);

        Set<String> existedTopRankKeywords = keywordRankRepository.getExistedTopRankKeywords();

        if (isDifferentKeywordsRank(existedTopRankKeywords, updatedTopRankKeywords)) {
            emitters.sendKeywordRankingMessage(updatedTopRankKeywords);
            keywordRankRepository.updateTopRankKeywords(updatedTopRankKeywords);
        }
    }

    private boolean isDifferentKeywordsRank(Set<String> origin, Set<String> update) {
        if (origin == null) {
            return true;
        }
        if (origin.size() != update.size()) {
            return true;
        }
        List<String> originList = origin.stream().toList();
        List<String> updateList = update.stream().toList();

        for (int i = 0; i < origin.size(); i++) {
            if (!originList.get(i).equals(updateList.get(i))) {
                return true;
            }
        }
        return false;
    }
}
