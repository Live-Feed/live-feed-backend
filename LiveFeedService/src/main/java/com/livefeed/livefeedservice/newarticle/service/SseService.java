package com.livefeed.livefeedservice.newarticle.service;

import com.livefeed.livefeedservice.newarticle.domain.Emitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    private final Emitters emitters;

    public SseEmitter register(String emitterKey) {
        return emitters.registerEmitter(emitterKey);
    }

    public String generateKey() {
        return emitters.generateSseKey();
    }
}
