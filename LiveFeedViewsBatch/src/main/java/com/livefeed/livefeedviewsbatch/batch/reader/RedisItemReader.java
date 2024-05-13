package com.livefeed.livefeedviewsbatch.batch.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.Assert;

public class RedisItemReader<K, V> implements ItemStreamReader<V> {

	protected final RedisTemplate<K, V> redisTemplate;

	private final ScanOptions scanOptions;

	protected Cursor<K> cursor;

	public RedisItemReader(RedisTemplate<K, V> redisTemplate, ScanOptions scanOptions) {
		Assert.notNull(redisTemplate, "redisTemplate must not be null");
		Assert.notNull(scanOptions, "scanOptions must no be null");
		this.redisTemplate = redisTemplate;
		this.scanOptions = scanOptions;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.cursor = this.redisTemplate.scan(this.scanOptions);
	}

	@Override
	public V read() throws Exception {
		if (this.cursor.hasNext()) {
			K nextKey = this.cursor.next();
			return this.redisTemplate.opsForValue().get(nextKey);
		}
		else {
			return null;
		}
	}

	@Override
	public void close() throws ItemStreamException {
		this.cursor.close();
	}

}