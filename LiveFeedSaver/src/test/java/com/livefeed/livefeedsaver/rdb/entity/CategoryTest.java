package com.livefeed.livefeedsaver.rdb.entity;

import com.livefeed.livefeedsaver.common.dto.Platform;
import com.livefeed.livefeedsaver.common.dto.Service;
import com.livefeed.livefeedsaver.common.dto.Theme;
import com.livefeed.livefeedsaver.kafka.consumer.dto.ConsumerKeyDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("from 정적 메서드로 인스턴스를 생성할때 isDeleted 값이 false로 기본 세팅되는지 확인하는 테스트")
    void checkIsDeleted() {
        // given
        // when
        Category category = Category.from(new ConsumerKeyDto(Service.ARTICLE, Platform.NAVER, Theme.SPORTS));

        // then
        assertThat(category.isDeleted()).isFalse();
    }
}