package com.livefeed.livefeedsaver.rdb.repository;

import com.livefeed.livefeedsaver.rdb.entity.PressCompany;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PressCompanyRepositoryTest {

    @Autowired
    private PressCompanyRepository pressCompanyRepository;

    @Test
    @DisplayName("언론사 이름을 통해 특정 언론사를 찾을 수 있습니다.")
    void findByServiceAndPlatformAndTheme() {
        // given
        PressCompany pressCompany = PressCompany.builder().companyName("test").build();
        pressCompanyRepository.saveAndFlush(pressCompany);
        // when
        Optional<PressCompany> result = pressCompanyRepository.findByCompanyName("test");
        // then
        assertThat(result).isNotEmpty();
    }

}