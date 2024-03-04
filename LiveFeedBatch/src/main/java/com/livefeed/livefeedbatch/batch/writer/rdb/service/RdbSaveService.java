package com.livefeed.livefeedbatch.batch.writer.rdb.service;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.domain.entity.Article;
import com.livefeed.livefeedbatch.batch.domain.entity.Category;
import com.livefeed.livefeedbatch.batch.domain.entity.PressCompany;
import com.livefeed.livefeedbatch.batch.domain.repository.ArticleRepository;
import com.livefeed.livefeedbatch.batch.domain.repository.CategoryRepository;
import com.livefeed.livefeedbatch.batch.domain.repository.PressCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RdbSaveService {

    private final PressCompanyRepository pressCompanyRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    public Article saveArticle(UrlInfo key, ParseResultDto value) {

        Category category = categoryRepository.findByServiceAndPlatformAndTheme(key.service(), key.platform(), key.theme())
                .orElseGet(() -> categoryRepository.save(Category.from(key)));

        PressCompany pressCompany = pressCompanyRepository.findByCompanyName(value.pressCompanyName())
                .orElseGet(() -> pressCompanyRepository.save(PressCompany.of(value.pressCompanyName())));

        Article article = Article.from(pressCompany, category, value);
        return articleRepository.save(article);
    }
}
