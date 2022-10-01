package com.spring.store.api.repository;

import com.spring.store.api.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    List<Tutorial> findByPublished(boolean published);
//
//    List<Tutorial> findByTitleContaining(String title);
}
