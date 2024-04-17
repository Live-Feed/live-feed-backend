package com.livefeed.livefeedservice.newarticle.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();

        String str = new String(body, StandardCharsets.UTF_8);

        log.info("body = {}", str);
        try {
            Set<Long> set = objectMapper.readValue(str, new TypeReference<>() {});
            log.info("message = {}", set);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
