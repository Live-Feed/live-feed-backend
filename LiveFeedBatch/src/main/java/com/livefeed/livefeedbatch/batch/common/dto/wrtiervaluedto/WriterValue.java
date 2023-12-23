package com.livefeed.livefeedbatch.batch.common.dto.wrtiervaluedto;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDtoInterface;

public record WriterValue(
        String url
) implements ItemDtoInterface<WriterValue> {

    @Override
    public WriterValue getValue() {
        return this;
    }
}
