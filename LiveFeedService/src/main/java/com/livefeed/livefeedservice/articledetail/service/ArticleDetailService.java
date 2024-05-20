package com.livefeed.livefeedservice.articledetail.service;

import com.livefeed.livefeedservice.articledetail.dto.ArticleDetailDto;
import com.livefeed.livefeedservice.rdb.entity.Article;
import com.livefeed.livefeedservice.rdb.repository.ArticleRepository;
import com.livefeed.livefeedservice.views.ViewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleDetailService {

    private final ArticleRepository articleRepository;
    private final ViewsService viewsService;

    public ArticleDetailDto findArticleDetail(Long articleId) {

        Article article = articleRepository.findArticleDetailById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 기사가 존재하지 않습니다."));

        article.setViews(viewsService.increase(articleId));

        return ArticleDetailDto.from(article);
    }
}
