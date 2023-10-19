package com.livefeed.livefeedsaver.rdb.service;

import com.livefeed.livefeedcommon.kafka.record.HtmlTopicKey;
import com.livefeed.livefeedcommon.kafka.record.HtmlTopicValue;
import com.livefeed.livefeedsaver.rdb.entity.Article;
import com.livefeed.livefeedsaver.rdb.entity.Category;
import com.livefeed.livefeedsaver.rdb.entity.PressCompany;
import com.livefeed.livefeedsaver.rdb.repository.ArticleRepository;
import com.livefeed.livefeedsaver.rdb.repository.CategoryRepository;
import com.livefeed.livefeedsaver.rdb.repository.PressCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RdbSaveService {

    private final PressCompanyRepository pressCompanyRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public Article saveArticle(HtmlTopicKey key, HtmlTopicValue value) {

        Category category = categoryRepository.findByServiceAndPlatformAndTheme(key.service(), key.platform(), key.theme())
                .orElseGet(() -> categoryRepository.save(Category.from(key)));

        PressCompany pressCompany = pressCompanyRepository.findByCompanyName(value.pressCompanyName())
                .orElseGet(() -> pressCompanyRepository.save(PressCompany.of(value.pressCompanyName())));

        Article article = Article.from(pressCompany, category, value);
        return articleRepository.save(article);
    }
}
