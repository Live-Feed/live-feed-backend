package com.livefeed.livefeedsaver.rdb.configuration;

import com.livefeed.livefeedsaver.common.dto.Platform;
import com.livefeed.livefeedsaver.common.dto.Service;
import com.livefeed.livefeedsaver.common.dto.Theme;
import com.livefeed.livefeedsaver.rdb.entity.Category;
import com.livefeed.livefeedsaver.rdb.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.livefeed.livefeedsaver.rdb"})
@RequiredArgsConstructor
public class JpaConfiguration {

    private final CategoryRepository categoryRepository;

    @Profile("local")
    @PostConstruct
    public void init() {
        Category category = Category.builder()
                .service(Service.ARTICLE)
                .platform(Platform.NAVER)
                .theme(Theme.SPORTS)
                .build();

        categoryRepository.save(category);
    }
}
