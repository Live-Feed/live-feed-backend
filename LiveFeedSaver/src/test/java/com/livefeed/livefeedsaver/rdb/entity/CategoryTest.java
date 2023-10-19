package com.livefeed.livefeedsaver.rdb.entity;

import com.livefeed.livefeedcommon.kafka.dto.Platform;
import com.livefeed.livefeedcommon.kafka.dto.Service;
import com.livefeed.livefeedcommon.kafka.dto.Theme;
import com.livefeed.livefeedcommon.kafka.record.HtmlTopicKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("from 정적 메서드로 인스턴스를 생성할때 isDeleted 값이 false로 기본 세팅되는지 확인하는 테스트")
    void checkIsDeleted() {
        // given
        // when
        Category category = Category.from(new HtmlTopicKey(Service.ARTICLE, Platform.NAVER, Theme.SPORTS));

        // then
        assertThat(category.isDeleted()).isFalse();
    }
}