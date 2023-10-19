package com.livefeed.livefeedsaver.rdb.repository;

import com.livefeed.livefeedcommon.kafka.dto.Platform;
import com.livefeed.livefeedcommon.kafka.dto.Service;
import com.livefeed.livefeedcommon.kafka.dto.Theme;
import com.livefeed.livefeedsaver.rdb.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByServiceAndPlatformAndTheme(Service service, Platform platform, Theme theme);
}
