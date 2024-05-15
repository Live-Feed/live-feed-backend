package com.livefeed.livefeedservice.newarticle.controller;

import com.livefeed.livefeedservice.newarticle.service.CookieService;
import com.livefeed.livefeedservice.newarticle.service.SseService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;
    private final CookieService cookieService;

    @GetMapping(value = "/register", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter register(HttpServletResponse response) {
        String emitterKey = sseService.generateKey();
        Cookie cookie = cookieService.makeSseKeyCookie(emitterKey);
        response.addCookie(cookie);
        log.info("new server sent event connect with sseKey = {}", emitterKey);
        return sseService.register(emitterKey);
    }
}
