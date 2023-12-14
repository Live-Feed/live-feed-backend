package com.livefeed.livefeedbatch.batch.writer.rdb.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.livefeed.livefeedbatch.batch.writer.rdb"})
@RequiredArgsConstructor
public class JpaConfiguration {
}
