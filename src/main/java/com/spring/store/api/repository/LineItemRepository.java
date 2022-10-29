package com.spring.store.api.repository;

import com.spring.store.api.models.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {
    List<LineItem> findByWishListId(Long wishListId);
}
