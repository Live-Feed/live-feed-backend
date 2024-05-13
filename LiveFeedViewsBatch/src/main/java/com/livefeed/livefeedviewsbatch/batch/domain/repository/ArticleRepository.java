package com.livefeed.livefeedviewsbatch.batch.domain.repository;

import com.livefeed.livefeedviewsbatch.batch.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
