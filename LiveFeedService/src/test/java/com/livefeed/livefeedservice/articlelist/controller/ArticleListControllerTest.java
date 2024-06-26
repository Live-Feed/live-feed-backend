package com.livefeed.livefeedservice.articlelist.controller;

import com.livefeed.livefeedservice.articlelist.dto.ArticleDto;
import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.articlelist.dto.SearchResultDto;
import com.livefeed.livefeedservice.articlelist.service.ArticleListService;
import com.livefeed.livefeedservice.articlelist.util.SearchQueryParam;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(value = ArticleListController.class)
class ArticleListControllerTest {

    private MockMvc mockMvc;

    private final String url = "/api/list/articles";

    @MockBean
    private ArticleListService articleListService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    @DisplayName("search 쿼리를 통해 기사를 검색할 수 있습니다.")
    void getArticleList() throws Exception {
        // given
        SearchResultDto searchResultDto = new SearchResultDto(List.of(
                new ArticleDto(5L, "article5", "press5", "content5", "photo5", "5분전", 0f),
                new ArticleDto(3L, "article3", "press3", "content3", "photo3", "3분전", 0f)
        ), "3eaGBAEIYXJ0aWNsZXMWOVpGTDhfYTh");

        ArticleListDto data = ArticleListDto.from(searchResultDto, 2);

        Mockito.doReturn(data).when(articleListService).getArticleList(Mockito.any(SearchQueryParam.class), Mockito.any());

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("type", "articleTitle,bodyHtml")
                .queryParam("keyword", "hello")
                .queryParam("size", "2")
                .queryParam("sort", "id-desc")
                .queryParam("lastId", "6")
                .queryParam("pit", "3eaGBAEIYXJ0aWNsZXMWOVpGTDhfYTh")
                .cookie(new Cookie("sseKey", "userSseKey"))
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("기사 목록 조회 성공했습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[0].articleId").value(5L))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[0].title").value("article5"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[0].pressCompany").value("press5"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[0].content").value("content5"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[0].photo").value("photo5"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[0].minutesAgo").value("5분전"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[1].articleId").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[1].title").value("article3"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[1].pressCompany").value("press3"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[1].content").value("content3"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[1].photo").value("photo3"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articles[1].minutesAgo").value("3분전"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.isLast").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("data.lastId").value(3L))
                .andExpect(MockMvcResultMatchers.jsonPath("data.pit").value("3eaGBAEIYXJ0aWNsZXMWOVpGTDhfYTh"));
    }
}