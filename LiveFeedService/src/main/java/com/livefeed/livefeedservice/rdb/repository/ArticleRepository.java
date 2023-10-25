package com.livefeed.livefeedservice.rdb.repository;

import com.livefeed.livefeedservice.rdb.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select ar from Article ar join fetch ar.pressCompany join fetch ar.category where ar.id = :id")
    Optional<Article> findArticleDetailById(@Param("id") Long id);
}
