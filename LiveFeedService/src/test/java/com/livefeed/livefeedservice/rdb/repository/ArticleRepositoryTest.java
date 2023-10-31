package com.livefeed.livefeedservice.rdb.repository;

import com.livefeed.livefeedservice.rdb.dto.Platform;
import com.livefeed.livefeedservice.rdb.dto.Service;
import com.livefeed.livefeedservice.rdb.dto.Theme;
import com.livefeed.livefeedservice.rdb.entity.Article;
import com.livefeed.livefeedservice.rdb.entity.Category;
import com.livefeed.livefeedservice.rdb.entity.PressCompany;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PressCompanyRepository pressCompanyRepository;

    @Test
    @DisplayName("fetch join 을 통해 기사 관련 내용들을 한번에 정상적으로 가져오는지 확인하는 테스트")
    void findArticleDetailById() {
        // given
        PressCompany pressCompany = PressCompany.builder()
                .companyName("test")
                .build();
        PressCompany savedPressCompany = pressCompanyRepository.saveAndFlush(pressCompany);

        Category category = Category.builder()
                .service(Service.ARTICLE)
                .platform(Platform.NAVER)
                .theme(Theme.SPORTS)
                .build();
        Category savedCategory = categoryRepository.saveAndFlush(category);

        Article article = Article.builder()
                .pressCompany(savedPressCompany)
                .category(savedCategory)
                .contentHeader("기사 헤더")
                .contentBody("기사 본문")
                .title("기사 제목")
                .build();
        Article savedArticle = articleRepository.saveAndFlush(article);

        // when
        Article targetArticle = articleRepository.findArticleDetailById(savedArticle.getId()).get();

        // then
        assertThat(targetArticle.getPressCompany()).isEqualTo(savedPressCompany);
        assertThat(targetArticle.getCategory()).isEqualTo(savedCategory);
        assertThat(targetArticle.getTitle()).isEqualTo("기사 제목");
    }
}