package com.livefeed.livefeedbatch.batch.domain.repository;

import com.livefeed.livefeedbatch.batch.domain.entity.PressCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PressCompanyRepository extends JpaRepository<PressCompany, Long> {

    Optional<PressCompany> findByCompanyName(String companyName);
}
