package com.livefeed.livefeedservice.common.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.livefeed.livefeedservice.rdb")
@Configuration
public class JpaConfiguration {
}
