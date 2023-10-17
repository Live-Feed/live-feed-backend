package com.livefeed.livefeedsaver.rdb.service;

import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerKeyDto;
import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerValueDto;
import com.livefeed.livefeedsaver.rdb.repository.ArticleRepository;
import com.livefeed.livefeedsaver.rdb.repository.CategoryRepository;
import com.livefeed.livefeedsaver.rdb.repository.PressCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RdbSaveService {

    private final PressCompanyRepository pressCompanyRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    public void saveArticle(ConsumerKeyDto key, ConsumerValueDto value) {
        // 카테고리 설정


        // 언론사 설정



    }
}
