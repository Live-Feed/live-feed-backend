package com.livefeed.livefeedservice.rdb.repository;

import com.livefeed.livefeedservice.rdb.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
