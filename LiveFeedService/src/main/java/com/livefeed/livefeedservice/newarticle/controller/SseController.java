package com.livefeed.livefeedservice.newarticle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedservice.newarticle.service.SseService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping(value = "/register", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter register(HttpServletResponse response) {
        String emitterKey = sseService.generateEmitterKey();
        response.addCookie(new Cookie("sseKey", emitterKey));
        return sseService.register(emitterKey);
    }

    @GetMapping("send")
    public String send() throws JsonProcessingException {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        RedisOperations<String, String> operations = opsForValue.getOperations();
        Set<Long> data = Set.of(1L, 2L);
        operations.convertAndSend("test-room", objectMapper.writeValueAsString(data));
        return "ok";
    }
}
