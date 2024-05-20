package com.livefeed.livefeedservice.views;

import com.livefeed.livefeedservice.rdb.entity.Article;
import com.livefeed.livefeedservice.rdb.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewsService {
    private final ArticleRepository articleRepository;
    private final RedisTemplate<Long, Integer> viewsRedisTemplate;
    public static final int DEFAULT_EXPIRE_DAYS = 1;

    public int getOrSet(Long key) {
        return Optional.ofNullable(viewsRedisTemplate.opsForValue().get(key))
                .orElseGet(() -> {
                    Article article = articleRepository.findById(key).orElseThrow();
                    return setAndGet(key, article.getViews());
                });
    }

    public int increase(Long key) {
        return Optional.ofNullable(viewsRedisTemplate.opsForValue().get(key))
                .map(views -> setAndGet(key, views + 1))
                .orElseGet(() -> {
                    Article article = articleRepository.findById(key).orElseThrow();
                    return setAndGet(key, article.getViews() + 1);
                });
    }

    private int setAndGet(Long key, int value) {
        viewsRedisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE_DAYS, TimeUnit.DAYS);
        return value;
    }
}
