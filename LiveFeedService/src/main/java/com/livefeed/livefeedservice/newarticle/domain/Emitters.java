package com.livefeed.livefeedservice.newarticle.domain;

import com.livefeed.livefeedservice.articlelist.repository.KeywordRankRepository;
import com.livefeed.livefeedservice.newarticle.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class Emitters {

    private static final long SSE_TIMEOUT = 10 * 60 * 1000L;
    private static final String ARTICLE_UPDATE_EVENT_NAME = "article update";
    private static final String NEW_ARTICLE_ALERT_MESSAGE = "new articles are registered";
    private static final String KEYWORD_RANKING_UPDATE_EVENT_NAME = "keywords ranking update";

    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private final KeywordRepository keywordRepository;
    private final KeywordRankRepository keywordRankRepository;

    public SseEmitter registerEmitter(String sseKey) {
        SseEmitter sseEmitter = new SseEmitter(SSE_TIMEOUT);

        emitters.put(sseKey, sseEmitter);

        sseEmitter.onCompletion(() -> {
            log.info("sse onCompletion sseKey = {}", sseKey);
            // server sent event 연결이 끝난다면 emitters 목록과 redis 에서 등록된 키워드를 삭제합니다.
            emitters.remove(sseKey);
            keywordRepository.deleteUserKeywords(sseKey);
        });

        sseEmitter.onTimeout(sseEmitter::complete);

        try {
            Set<String> topRankKeywords = keywordRankRepository.getTopRankKeywords();
            sseEmitter.send(SseEmitter.event().name(KEYWORD_RANKING_UPDATE_EVENT_NAME).data(topRankKeywords));
        } catch (IOException e) {
            emitters.remove(sseKey);
        }
        return sseEmitter;
    }

    public String generateSseKey() {
        return UUID.randomUUID().toString();
    }

    public List<String> getSseKeyList() {
        return List.copyOf(emitters.keySet());
    }

    public void sendAlertMessage(Set<String> terms, Map<String, Set<String>> userKeywordMap) {
        for (String sseKey : userKeywordMap.keySet()) {
            Set<String> keywords = userKeywordMap.get(sseKey);
            if (keywords == null) {
                return;
            }

            for (String keyword : keywords) {
                if (terms.contains(keyword)) {
                    sendAlertMessage(sseKey);
                    break;
                }
            }
        }
    }

    public void sendKeywordRankingMessageToAllUser(Set<String> keywordsRanking) {
        emitters.keySet().forEach(key -> {
            try {
                SseEmitter emitter = emitters.get(key);
                emitter.send(SseEmitter.event().name(KEYWORD_RANKING_UPDATE_EVENT_NAME).data(keywordsRanking));
            } catch (IOException e) {
                log.warn("브라우저가 닫혀 해당 sseKey 로 연결된 브라우저가 없습니다. sseKey = {}", key);
                emitters.remove(key);
            }
        });
    }

    private void sendAlertMessage(String sseKey) {
        SseEmitter sseEmitter = emitters.get(sseKey);
        try {
            sseEmitter.send(SseEmitter.event().id(sseKey).name(ARTICLE_UPDATE_EVENT_NAME).data(NEW_ARTICLE_ALERT_MESSAGE));
        } catch (IOException e) {
            log.warn("브라우저가 닫혀 해당 sseKey 로 연결된 브라우저가 없습니다. sseKey = {}", sseKey);
            emitters.remove(sseKey);
        }
    }
}



















