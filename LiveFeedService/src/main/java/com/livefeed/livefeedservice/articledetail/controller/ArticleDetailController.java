package com.livefeed.livefeedservice.articledetail.controller;

import com.livefeed.livefeedservice.common.dto.SuccessResponse;
import com.livefeed.livefeedservice.articledetail.dto.ArticleDetailDto;
import com.livefeed.livefeedservice.articledetail.service.ArticleDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/detail")
@RequiredArgsConstructor
public class ArticleDetailController {

    private final ArticleDetailService articleDetailService;

    @GetMapping("/articles/{articleId}")
    public SuccessResponse<ArticleDetailDto> getArticleDetail(@PathVariable("articleId") Long articleId) {
        log.info("article detail controller, articleId = {}", articleId);
        ArticleDetailDto articleDetail = articleDetailService.findArticleDetail(articleId);
        return SuccessResponse.ok("기사 상세 조회 성공했습니다.", articleDetail);
    }
}
