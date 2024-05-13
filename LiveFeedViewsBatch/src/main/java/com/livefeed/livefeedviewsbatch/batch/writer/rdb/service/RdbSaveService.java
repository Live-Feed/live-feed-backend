package com.livefeed.livefeedviewsbatch.batch.writer.rdb.service;

import com.livefeed.livefeedviewsbatch.batch.domain.entity.Article;
import com.livefeed.livefeedviewsbatch.batch.domain.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RdbSaveService {
    private final ArticleRepository articleRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setViews(Long articleId, int views) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.setViews(views);
    }
}
