package com.livefeed.livefeedsaver.rdb.service;

import com.livefeed.livefeedsaver.common.dto.Platform;
import com.livefeed.livefeedsaver.common.dto.Service;
import com.livefeed.livefeedsaver.common.dto.Theme;
import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerValueDto;
import com.livefeed.livefeedsaver.rdb.entity.Article;
import com.livefeed.livefeedsaver.rdb.entity.Category;
import com.livefeed.livefeedsaver.rdb.entity.PressCompany;
import com.livefeed.livefeedsaver.rdb.repository.ArticleRepository;
import com.livefeed.livefeedsaver.rdb.repository.CategoryRepository;
import com.livefeed.livefeedsaver.rdb.repository.PressCompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RdbSaveServiceTest {

    private RdbSaveService rdbSaveService;

    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private PressCompanyRepository pressCompanyRepository;

    private ConsumerKeyDto key;
    private ConsumerValueDto value;

    @BeforeEach
    void setKeyValue() {
        key = new ConsumerKeyDto(Service.ARTICLE, Platform.NAVER, Theme.SPORTS);
        value = new ConsumerValueDto("title", "publication time", "동아일보", "기자이름", "기자 이메일", "원본 url", "header html", "body html");
        rdbSaveService = new RdbSaveService(pressCompanyRepository, categoryRepository, articleRepository);
    }
    
    @Test
    @DisplayName("기존에 없던 카테고리 데이터가 들어온다면 카테고리를 새로 만들어주고 데이터를 저장합니다.")
    void saveRdbArticleWithNewCategory() {
        // given
        PressCompany pressCompany = PressCompany.of("동아일보");
        pressCompanyRepository.saveAndFlush(pressCompany);
        // when
        Article savedArticle = rdbSaveService.saveArticle(key, value);
        // then
        Optional<Category> category = categoryRepository.findByServiceAndPlatformAndTheme(Service.ARTICLE, Platform.NAVER, Theme.SPORTS);
        assertThat(category).isNotEmpty();

        Optional<Article> article = articleRepository.findById(savedArticle.getId());
        assertThat(article).isNotEmpty();
    }
    
    @Test
    @DisplayName("기존에 없던 언론사 데이터가 들어온다면 언론사를 새로 만들어주고 데이터를 저장합니다.")
    void saveRdbArticleWithNewPressCompany() {
        // given
        Category category = Category.from(key);
        categoryRepository.saveAndFlush(category);
        // when
        Article savedArticle = rdbSaveService.saveArticle(key, value);
        // then
        Optional<PressCompany> pressCompany = pressCompanyRepository.findByCompanyName(value.pressCompanyName());
        assertThat(pressCompany).isNotEmpty();

        Optional<Article> article = articleRepository.findById(savedArticle.getId());
        assertThat(article).isNotEmpty();
    }
    
    @Test
    @DisplayName("정상적인 데이터가 들어온다면 그대로 데이터를 저장합니다.")
    void saveRdbArticle() {
        // given
        Category category = Category.from(key);
        categoryRepository.saveAndFlush(category);
        PressCompany pressCompany = PressCompany.of("동아일보");
        pressCompanyRepository.saveAndFlush(pressCompany);
        // when
        Article savedArticle = rdbSaveService.saveArticle(key, value);
        // then
        Optional<Article> article = articleRepository.findById(savedArticle.getId());
        assertThat(article).isNotEmpty();
    }
    
}