package com.livefeed.livefeedbatch.batch.writer.rdb.service;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.writer.rdb.entity.Article;
import com.livefeed.livefeedbatch.batch.writer.rdb.entity.Category;
import com.livefeed.livefeedbatch.batch.writer.rdb.entity.PressCompany;
import com.livefeed.livefeedbatch.batch.writer.rdb.repository.ArticleRepository;
import com.livefeed.livefeedbatch.batch.writer.rdb.repository.CategoryRepository;
import com.livefeed.livefeedbatch.batch.writer.rdb.repository.PressCompanyRepository;
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
    public Article saveArticle(UrlInfo key, ParseResultDto value) {

        Category category = categoryRepository.findByServiceAndPlatformAndTheme(key.service(), key.platform(), key.theme())
                .orElseGet(() -> categoryRepository.save(Category.from(key)));

        PressCompany pressCompany = pressCompanyRepository.findByCompanyName(value.pressCompanyName())
                .orElseGet(() -> pressCompanyRepository.save(PressCompany.of(value.pressCompanyName())));

        Article article = Article.from(pressCompany, category, value);
        return articleRepository.save(article);
    }
}
