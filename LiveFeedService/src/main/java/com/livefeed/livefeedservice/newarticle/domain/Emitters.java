package com.livefeed.livefeedservice.newarticle.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class Emitters {

    private static final long SSE_TIMEOUT = 60 * 60 * 1000L;
    private static final String EVENT_NAME = "article update";
    private static final String NEW_ARTICLE_ALERT_MESSAGE = "new articles are registered";
    private static final String INITIAL_MESSAGE = "sse connected";

    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter registerEmitter(String sseKey) {
        SseEmitter sseEmitter = new SseEmitter(SSE_TIMEOUT);

        emitters.put(sseKey, sseEmitter);

        sseEmitter.onCompletion(() -> {
            log.info("{} sse onCompletion", sseKey);
            sseEmitter.complete();
            emitters.remove(sseKey);
        });

        sseEmitter.onTimeout(sseEmitter::complete);

        try {
            sseEmitter.send(SseEmitter.event().id(sseKey).data(INITIAL_MESSAGE));
        } catch (IOException e) {
            emitters.remove(sseKey);
            throw new RuntimeException(e);
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

    private void sendAlertMessage(String sseKey) {
        SseEmitter sseEmitter = emitters.get(sseKey);
        if (sseEmitter == null) {
            log.info("이미 삭제된 sseKey 입니다. sseKey = {}", sseKey);
            return;
        }

        try {
            sseEmitter.send(SseEmitter.event().id(sseKey).name(EVENT_NAME).data(NEW_ARTICLE_ALERT_MESSAGE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



















