package com.livefeed.livefeedbatch.batch.writer.rdb.repository;

import com.livefeed.livefeedbatch.batch.writer.rdb.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
