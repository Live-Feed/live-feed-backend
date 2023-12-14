package com.livefeed.livefeedbatch.urlcrawler.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class NewsUrlWriter implements ItemWriter<String> {

    @Override
    public void write(Chunk<? extends String> chunk) {
        for (String articleUrl : chunk.getItems()) {
            log.info("articleUrl = {}", articleUrl);
            // TODO: 12/15/23 희원 : 다음 스탭으로 UrlInfo 값과 articleUrl 값을 넘겨주어야 합니다.
        }
    }
}