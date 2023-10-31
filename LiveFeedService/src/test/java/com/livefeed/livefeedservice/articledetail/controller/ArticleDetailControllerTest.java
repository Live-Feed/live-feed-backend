package com.livefeed.livefeedservice.articledetail.controller;

import com.livefeed.livefeedservice.articledetail.dto.ArticleDetailDto;
import com.livefeed.livefeedservice.articledetail.service.ArticleDetailService;
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


@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest
class ArticleDetailControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ArticleDetailService articleDetailService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    @DisplayName("존재하지 않는 기사를 요청하는 경우 400번 에러를 발생합니다.")
    void IllegalArticleId() throws Exception {
        // given
        Long articleId = 100L;
        Mockito.doThrow(new IllegalArgumentException("해당하는 기사가 없습니다.")).when(articleDetailService).findArticleDetail(articleId);
        String url = "/api/detail/articles/{articleId}";
        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(url, "100")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("해당하는 기사가 없습니다."))
                .andDo(
                        MockMvcRestDocumentation.document("article-detail-error",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                RequestDocumentation.pathParameters(
                                        RequestDocumentation.parameterWithName("articleId").description("뉴스 기사 Id")
                                ),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태값"),
                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("상세 메시지")
                                )
                        )
                );
    }

    @DisplayName("기사 Id를 통해 기사를 요청하여 기사 상세 내용을 받을 수 있습니다.")
    @Test
    void getArticleDetail() throws Exception {
        // given
        Long articleId = 1L;
        ArticleDetailDto data = getArticleDetailDto();
        Mockito.doReturn(data).when(articleDetailService).findArticleDetail(articleId);
        String url = "/api/detail/articles/{articleId}";
        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(url, articleId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("기사 상세 조회 성공했습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articleId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("data.title").value("기사 제목"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.pressCompany").value("언론사 이름"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.reporter").value("기자 이름"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.publicationTime").value("발행 시간"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.articleUrl").value("기사 원본 url"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.contentHeader").value("기사 헤더 내용"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.contentBody").value("기사 본문 내용"))
                .andDo(
                        MockMvcRestDocumentation.document("article-detail",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                RequestDocumentation.pathParameters(
                                        RequestDocumentation.parameterWithName("articleId").description("뉴스 기사 Id")
                                ),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태값"),
                                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("상세 메시지"),
                                        PayloadDocumentation.fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        PayloadDocumentation.fieldWithPath("data.articleId").type(JsonFieldType.NUMBER).description("기사 ID"),
                                        PayloadDocumentation.fieldWithPath("data.title").type(JsonFieldType.STRING).description("기사 제목"),
                                        PayloadDocumentation.fieldWithPath("data.pressCompany").type(JsonFieldType.STRING).description("언론사 이름"),
                                        PayloadDocumentation.fieldWithPath("data.reporter").type(JsonFieldType.STRING).description("기자 이름"),
                                        PayloadDocumentation.fieldWithPath("data.publicationTime").type(JsonFieldType.STRING).description("발행 시간"),
                                        PayloadDocumentation.fieldWithPath("data.articleUrl").type(JsonFieldType.STRING).description("기사 원본 url"),
                                        PayloadDocumentation.fieldWithPath("data.contentHeader").type(JsonFieldType.STRING).description("기사 헤더 내용"),
                                        PayloadDocumentation.fieldWithPath("data.contentBody").type(JsonFieldType.STRING).description("기사 본문 내용")
                                )
                        )
                );
    }

    private ArticleDetailDto getArticleDetailDto() {
        return new ArticleDetailDto(
                1L, "기사 제목", "언론사 이름",
                "기자 이름", "발행 시간",
                "기사 원본 url", "기사 헤더 내용", "기사 본문 내용");
    }
}