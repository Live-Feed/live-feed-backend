package com.livefeed.livefeedviewsbatch.batch.reader;

import com.livefeed.livefeedviewsbatch.batch.common.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ViewsReader extends RedisItemReader {

    public ViewsReader(RedisTemplate redisTemplate) {
        super(redisTemplate, ScanOptions.NONE);
    }

    @Override
    public ItemDto read() {
        if (cursor.hasNext()) {
            Long nextKey = (Long) cursor.next();
            int value = Optional.ofNullable(redisTemplate.opsForValue().get(nextKey))
                    .map(existingValue -> (int) existingValue)
                    .orElse(0);
            return new ItemDto(nextKey, value);
        }
        else {
            return null;
        }
    }
}
