package com.livefeed.livefeedbatch.batch.domain.repository;

import com.livefeed.livefeedbatch.batch.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
