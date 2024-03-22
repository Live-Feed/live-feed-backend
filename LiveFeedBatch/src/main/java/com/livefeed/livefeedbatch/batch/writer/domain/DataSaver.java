package com.livefeed.livefeedbatch.batch.writer.domain;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.processorvaluedto.ParseResultDto;
import com.livefeed.livefeedbatch.batch.domain.entity.Article;
import com.livefeed.livefeedbatch.batch.writer.rdb.service.RdbSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSaver {

    private final RdbSaveService rdbSaveService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Article saveArticle(UrlInfo key, ParseResultDto value) {
        return rdbSaveService.saveArticle(key, value);
    }
}
