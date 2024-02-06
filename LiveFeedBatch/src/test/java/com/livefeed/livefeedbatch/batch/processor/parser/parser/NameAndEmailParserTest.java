package com.livefeed.livefeedbatch.batch.processor.parser.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(result.getFirst()).isEqualTo("홍길동 기자");
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
        assertThat(result.getFirst()).isEqualTo("홍길동 기자");
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
        assertThat(result.getFirst()).isEqualTo("홍길동 기자");
        assertThat(result.getSecond()).isEqualTo("hong@example.com");
    }

    @Test
    @DisplayName("이름 언론사 기자(이메일) 형식의 패턴을 찾는지 확인하는 테스트")
    void findPattern4() {
        // given
        String text = "홍길동 MK스포츠 기자(hong@example.com)";
        // when
        Pair<String, String> result = nameAndEmailParser.extractNameAndEmail(text);
        // then
        assertThat(result.getFirst()).isEqualTo("홍길동 MK스포츠 기자");
        assertThat(result.getSecond()).isEqualTo("hong@example.com");
    }

    @Test
    @DisplayName("이름 기자 (이메일) 형식의 패턴을 찾는지 확인하는 테스트")
    void findPattern5() {
        // given
        String text = "홍길동 기자 (hong@example.com)";
        // when
        Pair<String, String> result = nameAndEmailParser.extractNameAndEmail(text);
        // then
        assertThat(result.getFirst()).isEqualTo("홍길동 기자");
        assertThat(result.getSecond()).isEqualTo("hong@example.com");
    }

    @Test
    @DisplayName("홍길동기자 (이메일) 형식의 패턴을 찾는지 확인하는 테스트")
    void findPattern6() {
        // given
        String text = "홍길동기자 (hong@example.com)";
        // when
        Pair<String, String> result = nameAndEmailParser.extractNameAndEmail(text);
        // then
        assertThat(result.getFirst()).isEqualTo("홍길동기자");
        assertThat(result.getSecond()).isEqualTo("hong@example.com");
    }

    @DisplayName("이름 형식의 패턴을 찾는지 확인하는 테스트")
    @Test
    void findPattern7() {
        // given
        String text = "홍길동";
        // when
        Pair<String, String> result = nameAndEmailParser.extractNameAndEmail(text);
        // then
        assertThat(result.getFirst()).isEqualTo("홍길동 기자");
        assertThat(result.getSecond()).isBlank();
    }
}