package com.livefeed.livefeedbatch.batch.domain.repository;


import com.livefeed.livefeedbatch.batch.domain.entity.Page;
import com.livefeed.livefeedbatch.batch.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {

    List<Page> findAllByCategory(Category category);
}
