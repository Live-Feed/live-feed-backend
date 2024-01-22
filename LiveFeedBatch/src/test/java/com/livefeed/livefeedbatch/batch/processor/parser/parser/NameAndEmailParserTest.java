package com.livefeed.livefeedbatch.batch.processor.parser.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class NameAndEmailParserTest {

    private NameAndEmailParser nameAndEmailParser;

    @BeforeEach
    void setUp() {
        nameAndEmailParser = new NameAndEmailParser();
    }

    @DisplayName("이름 (이메일) 형식의 패턴을 찾는지 확인하는 테스트")
    @Test
    void findPattern1() {
        // given
        String text = "홍길동 (hong@example.com)";
        // when
        Pair<String, String> result = nameAndEmailParser.extractNameAndEmail(text);
        // then
        assertThat(result.getFirst()).isEqualTo("홍길동");
        assertThat(result.getSecond()).isEqualTo("hong@example.com");
    }

    @DisplayName("이름(이메일) 형식의 패턴을 찾는지 확인하는 테스트")
    @Test
    void findPattern2() {
        // given
        String text = "홍길동(hong@example.com)";
        // when
        Pair<String, String> result = nameAndEmailParser.extractNameAndEmail(text);
        // then
        assertThat(result.getFirst()).isEqualTo("홍길동");
        assertThat(result.getSecond()).isEqualTo("hong@example.com");
    }

    @DisplayName("이름 이메일 형식의 패턴을 찾는지 확인하는 테스트")
    @Test
    void findPattern3() {
        // given
        String text = "홍길동 hong@example.com";
        // when
        Pair<String, String> result = nameAndEmailParser.extractNameAndEmail(text);
        // then
        assertThat(result.getFirst()).isEqualTo("홍길동");
        assertThat(result.getSecond()).isEqualTo("hong@example.com");
    }
}