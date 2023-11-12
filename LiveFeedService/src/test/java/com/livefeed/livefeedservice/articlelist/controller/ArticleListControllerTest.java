package com.livefeed.livefeedservice.articlelist.controller;

import com.livefeed.livefeedservice.articlelist.dto.ArticleDto;
import com.livefeed.livefeedservice.articlelist.dto.ArticleListDto;
import com.livefeed.livefeedservice.articlelist.service.ArticleListService;
import com.livefeed.livefeedservice.common.util.SearchQueryParam;
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
    @DisplayName("정렬 정보가 올바르지 않은 경우 400 에러가 발생합니다.")
    void inValidSortInfo() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(url)
                .queryParam("sort", "id:desc")
                .queryParam("size", "4")
                .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("올바르지 않은 정렬 정보입니다."))
                .andDo(
                        MockMvcRestDocumentation.document("article-list-error",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태값"),
                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("상세 메시지")
                                )
                        )
                );
    }

    @Test
    @DisplayName("search 쿼리를 통해 기사를 검색할 수 있습니다.")
    void getArticleList() throws Exception {
        // given
        ArticleListDto data = ArticleListDto.of(
                List.of(
                        new ArticleDto(5L, "article5", "press5", "content5", "photo5", "5분전"),
                        new ArticleDto(3L, "article3", "press3", "content3", "photo3", "3분전")
                ),
                false,
                3L,
                "3eaGBAEIYXJ0aWNsZXMWOVpGTDhfYTh"
        );

        Mockito.doReturn(data).when(articleListService).getArticleList(Mockito.any(SearchQueryParam.class));

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("type", "articleTitle,bodyHtml")
                .queryParam("keyword", "hello")
                .queryParam("size", "2")
                .queryParam("sort", "id-desc")
                .queryParam("lastId", "6")
                .queryParam("pit", "3eaGBAEIYXJ0aWNsZXMWOVpGTDhfYTh")
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
                .andExpect(MockMvcResultMatchers.jsonPath("data.pit").value("3eaGBAEIYXJ0aWNsZXMWOVpGTDhfYTh"))
                .andDo(
                        MockMvcRestDocumentation.document("article-list",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                RequestDocumentation.queryParameters(
                                        RequestDocumentation.parameterWithName("type").description("제목/본문 어디서 검색할지에 대한 조건").optional(),
                                        RequestDocumentation.parameterWithName("keyword").description("검색하고자 하는 키워드").optional(),
                                        RequestDocumentation.parameterWithName("size").description("한번에 검색하는 목록 개수").optional(),
                                        RequestDocumentation.parameterWithName("sort").description("정렬 기준 (id-desc)").optional(),
                                        RequestDocumentation.parameterWithName("lastId").description("마지막으로 조회된 기사 id 값").optional(),
                                        RequestDocumentation.parameterWithName("pit").description("기존 검색에서 전달받은 pit 값").optional()
                                ),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태값"),
                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("상세 메시지"),
                                        PayloadDocumentation.fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        PayloadDocumentation.fieldWithPath("data.articles[0].articleId").type(JsonFieldType.NUMBER).description("기사 아이디"),
                                        PayloadDocumentation.fieldWithPath("data.articles[0].title").type(JsonFieldType.STRING).description("기사 제목"),
                                        PayloadDocumentation.fieldWithPath("data.articles[0].pressCompany").type(JsonFieldType.STRING).description("언론사 이름"),
                                        PayloadDocumentation.fieldWithPath("data.articles[0].content").type(JsonFieldType.STRING).description("기사 본문"),
                                        PayloadDocumentation.fieldWithPath("data.articles[0].photo").type(JsonFieldType.STRING).description("기사의 첫 번째 사진"),
                                        PayloadDocumentation.fieldWithPath("data.articles[0].minutesAgo").type(JsonFieldType.STRING).description("기사의 발행 시간"),
                                        PayloadDocumentation.fieldWithPath("data.isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지인지 여부"),
                                        PayloadDocumentation.fieldWithPath("data.lastId").type(JsonFieldType.NUMBER).description("응답 기사 목록 중 마지막 기사의 id"),
                                        PayloadDocumentation.fieldWithPath("data.pit").type(JsonFieldType.STRING).description("해당 검색에 해당하는 point in time 값")

                                )
                        )
                );
    }
}