package com.livefeed.livefeedsaver.rdb.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.livefeed.livefeedsaver.rdb"})
@RequiredArgsConstructor
public class JpaConfiguration {
}
