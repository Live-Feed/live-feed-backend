package com.livefeed.livefeedsaver.rdb.repository;

import com.livefeed.livefeedsaver.rdb.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
