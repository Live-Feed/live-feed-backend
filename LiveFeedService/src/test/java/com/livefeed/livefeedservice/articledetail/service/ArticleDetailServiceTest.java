package com.livefeed.livefeedservice.articledetail.service;

import com.livefeed.livefeedservice.articledetail.dto.ArticleDetailDto;
import com.livefeed.livefeedservice.rdb.dto.Platform;
import com.livefeed.livefeedservice.rdb.dto.Service;
import com.livefeed.livefeedservice.rdb.dto.Theme;
import com.livefeed.livefeedservice.rdb.entity.Article;
import com.livefeed.livefeedservice.rdb.entity.Category;
import com.livefeed.livefeedservice.rdb.entity.PressCompany;
import com.livefeed.livefeedservice.rdb.repository.ArticleRepository;
import com.livefeed.livefeedservice.rdb.repository.CategoryRepository;
import com.livefeed.livefeedservice.rdb.repository.PressCompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class ArticleDetailServiceTest {
    
    @Autowired
    private ArticleDetailService articleDetailService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PressCompanyRepository pressCompanyRepository;
    
    @Test
    @DisplayName("기사 아이디가 존재하지 않는 경우 IllegalArgumentException을 발생시킵니다.")
    void findArticleByIllegalId() {
        // given
        Long testId = 100L;

        // when // then
        assertThatThrownBy(() -> articleDetailService.findArticleDetail(testId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 기사가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("기사 아이디가 존재하는 경우 정상적으로 기사 정보를 가져옵니다.")
    void findArticleById() {
        // given
        PressCompany pressCompany = savePressCompany();
        Category category = saveCategory();
        Article article = saveArticle(pressCompany, category);
        // when
        ArticleDetailDto result = articleDetailService.findArticleDetail(article.getId());
        // then
        assertThat(result).extracting("title", "pressCompany", "reporter", "publicationTime", "articleUrl", "contentHeader", "contentBody")
                .containsExactly("기사 제목", "test", "기자 이름", "기사 발행 시간", "기사 원문 url", "기사 헤더", "기사 본문");
    }

    private PressCompany savePressCompany() {
        PressCompany pressCompany = PressCompany.builder()
                .companyName("test")
                .build();
        return pressCompanyRepository.saveAndFlush(pressCompany);
    }

    private Category saveCategory() {
        Category category = Category.builder()
                .service(Service.ARTICLE)
                .platform(Platform.NAVER)
                .theme(Theme.SPORTS)
                .build();
        return categoryRepository.saveAndFlush(category);
    }

    private Article saveArticle(PressCompany savedPressCompany, Category savedCategory) {
        Article article = Article.builder()
                .pressCompany(savedPressCompany)
                .category(savedCategory)
                .contentHeader("기사 헤더")
                .contentBody("기사 본문")
                .title("기사 제목")
                .journalistName("기자 이름")
                .publicationTime("기사 발행 시간")
                .originArticleUrl("기사 원문 url")
                .build();
        return articleRepository.saveAndFlush(article);
    }

}