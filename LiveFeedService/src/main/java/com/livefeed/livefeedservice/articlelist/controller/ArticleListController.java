package com.livefeed.livefeedservice.articlelist.controller;

import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.articlelist.service.ArticleListService;
import com.livefeed.livefeedservice.common.dto.SuccessResponse;
import com.livefeed.livefeedservice.common.util.SearchQueryParam;
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
            @RequestParam(value = "type", required = false) List<String> type,
            @RequestParam(value = "keyword", required = false) List<String> keywords,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "id-desc") List<String> sorts,
            @RequestParam(value = "lastId", required = false) Long lastArticleId,
            @RequestParam(value = "pit", required = false) String pit
            ) {

        SearchQueryParam searchQueryParam = SearchQueryParam.makeParam(type, keywords, size, sorts, lastArticleId, pit);
        log.info("search article list controller, search param = {}", searchQueryParam);
        ArticleListDto data = articleListService.getArticleList(searchQueryParam);

        return SuccessResponse.ok("기사 목록 조회 성공했습니다.", data);
    }
}
