package com.livefeed.livefeedservice.articlelist.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livefeed.livefeedservice.articlelist.domain.repository.KeywordRankRepository;
import com.livefeed.livefeedservice.newarticle.domain.Emitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RedisKeywordSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final KeywordRankRepository keywordRankRepository;
    private final Emitters emitters;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        byte[] body = message.getBody();
        String str = new String(body, StandardCharsets.UTF_8);

        try {
            List<String> newArticleIds = objectMapper.readValue(str, new TypeReference<>() {});
            log.info("redis keyword subscribe message = {}", newArticleIds);

            keywordRankRepository.updateKeywordScore(newArticleIds);

            Set<String> updatedTopRankKeywords = keywordRankRepository.getTopRankKeywords();
            log.info("updatedTopRankKeywords = {}", updatedTopRankKeywords);

            Set<String> existedTopRankKeywords = keywordRankRepository.getExistedTopRankKeywords();

            if (isDifferentKeywordsRank(existedTopRankKeywords, updatedTopRankKeywords)) {
                emitters.sendKeywordRankingMessageToAllUser(updatedTopRankKeywords);
                keywordRankRepository.updateTopRankKeywords(updatedTopRankKeywords);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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
