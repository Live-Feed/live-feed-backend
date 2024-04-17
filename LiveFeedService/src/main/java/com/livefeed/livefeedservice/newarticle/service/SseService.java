package com.livefeed.livefeedservice.newarticle.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseService {

    private static final long SSE_TIMEOUT = 60 * 60 * 1000L;

    // key : emitterKey
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter register(String emitterKey) {
        SseEmitter sseEmitter = new SseEmitter(SSE_TIMEOUT);
        emitters.put(emitterKey, sseEmitter);

        sseEmitter.onCompletion(() -> {
            log.info("sse onCompletion");
            sseEmitter.complete();
            emitters.remove(emitterKey);
        });

        sseEmitter.onTimeout(sseEmitter::complete);

        try {
            sseEmitter.send(SseEmitter.event().id(emitterKey).name("sse").data("test data"));
        } catch (IOException e) {
            emitters.remove(emitterKey);
            throw new RuntimeException(e);
        }
        return sseEmitter;
    }

    public void send(String emitterKey) {
        SseEmitter sseEmitter = emitters.get(emitterKey);
//        sseEmitter.
        try {
            sseEmitter.send("테스트입니다.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send() {
        emitters.keySet().forEach(key -> {
            try {
                emitters.get(key).send("테스트입니다.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String generateEmitterKey() {
        return UUID.randomUUID().toString();
    }
}
