package com.livefeed.livefeedservice.articlelist.controller;

import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.articlelist.service.ArticleListService;
import com.livefeed.livefeedservice.common.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/list")
@RequiredArgsConstructor
public class ArticleListController {

    private final ArticleListService articleListService;

    @GetMapping("/articles")
    public SuccessResponse<ArticleListDto> getArticleList(
            @RequestParam(value = "keyword", required = false) List<String> keywords,
            @RequestParam(value = "category", required = false) List<String> categories
    ) {


        return null;
    }
}
