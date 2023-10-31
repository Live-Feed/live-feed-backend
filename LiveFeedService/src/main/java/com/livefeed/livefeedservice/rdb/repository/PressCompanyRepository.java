package com.livefeed.livefeedservice.rdb.repository;

import com.livefeed.livefeedservice.rdb.entity.PressCompany;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PressCompanyRepository extends JpaRepository<PressCompany, Long> {
}
