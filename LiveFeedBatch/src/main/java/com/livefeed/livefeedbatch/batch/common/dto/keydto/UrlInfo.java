package com.livefeed.livefeedbatch.batch.common.dto.keydto;

import com.livefeed.livefeedbatch.batch.common.dto.ItemDtoInterface;

/**
 * 카프카에서 key 값으로 사용하던 dto입니다.
 * 배치의 각 단계에서 반드시 넘겨야 하는 값입니다.
 * 아래 클래스와 value 객체를 kafka에서 넘겼는데
 * 배치로 수정하면서 key 객체와 value 객체를 하나의 객체로 묶어서 보내는 형식으로 수정했습니다.
 *
 */
public record UrlInfo(
        Service service,
        Platform platform,
        Theme theme
) implements ItemDtoInterface {
}
