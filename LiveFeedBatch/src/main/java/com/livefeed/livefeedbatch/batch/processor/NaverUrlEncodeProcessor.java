package com.livefeed.livefeedbatch.batch.processor;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDto;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.Platform;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.Service;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.Theme;
import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;
import com.livefeed.livefeedbatch.batch.common.dto.wrtiervaluedto.WriterValue;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class NaverUrlEncodeProcessor implements ItemProcessor<String, ItemDto> {

    @Override
    public ItemDto process(String item) throws Exception {
        return new ItemDto(new UrlInfo(Service.ARTICLE, Platform.NAVER, Theme.SPORTS), new WriterValue(item));
    }
}
