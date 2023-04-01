package com.spring.store.api.repository;

import com.spring.store.api.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long>, JpaRepository<Category, Long> {
//    List<Tutorial> findByPublished(boolean published);
//
//    List<Tutorial> findByTitleContaining(String title);
}
