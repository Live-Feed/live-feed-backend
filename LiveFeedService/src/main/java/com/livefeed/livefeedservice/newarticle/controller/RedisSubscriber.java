package com.livefeed.livefeedservice.newarticle.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedservice.newarticle.service.TermAnalyzeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final TermAnalyzeService termAnalyzeService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        String str = new String(body, StandardCharsets.UTF_8);

        try {
            Set<String> newArticleIds = objectMapper.readValue(str, new TypeReference<>() {});
            log.info("redis subscribe message = {}", newArticleIds);
            termAnalyzeService.noticeNewArticles(newArticleIds);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
