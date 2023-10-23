package com.livefeed.livefeedsaver.rdb.repository;

import com.livefeed.livefeedcommon.kafka.dto.Platform;
import com.livefeed.livefeedcommon.kafka.dto.Service;
import com.livefeed.livefeedcommon.kafka.dto.Theme;
import com.livefeed.livefeedsaver.rdb.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("service, platform, theme 컬럼을 조건으로 select 쿼리를 실행합니다.")
    @Test
    void findByServiceAndPlatformAndTheme() {
        // given
        Category category = Category.builder()
                .service(Service.ARTICLE)
                .platform(Platform.NAVER)
                .theme(Theme.SPORTS)
                .build();
        categoryRepository.saveAndFlush(category);
        // when
        Optional<Category> result = categoryRepository.findByServiceAndPlatformAndTheme(Service.ARTICLE, Platform.NAVER, Theme.SPORTS);
        // then
        assertThat(result).isNotEmpty();
    }
}