package com.livefeed.livefeedbatch.batch.common.dto;

import com.livefeed.livefeedbatch.batch.common.dto.keydto.UrlInfo;

public record ItemDto(
        UrlInfo urlInfo,
        ItemDtoInterface itemDtoInterface
) {
}
