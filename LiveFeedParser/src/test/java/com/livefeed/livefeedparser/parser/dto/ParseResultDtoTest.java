package com.livefeed.livefeedparser.parser.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ParseResultDtoTest {

    @DisplayName("header와 body의 html 내용을 div 태그로 합쳐서 잘 조합하는지 확인하는 테스트")
    @Test
    void combineHeaderAndBodyHtml() {
        // given
        HeaderDto headerDto = HeaderDto.of("<div>header</div>", "title", "pressName", "10:50", "http");
        BodyDto bodyDto = BodyDto.of("<div>body</div>", "journalistName", "journalEmail");
        // when
        ParseResultDto result = ParseResultDto.from(headerDto, bodyDto);
        // then
        String expectedResult = "<div class=\"article\">\n\t<div>header</div>\n\t<div>body</div>\n</div>";
        assertThat(result.html()).isEqualTo(expectedResult);
    }
}