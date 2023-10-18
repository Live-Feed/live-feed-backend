package com.livefeed.livefeedsaver.rdb.repository;

import com.livefeed.livefeedsaver.rdb.entity.PressCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PressCompanyRepository extends JpaRepository<PressCompany, Long> {

    Optional<PressCompany> findByCompanyName(String companyName);
}
