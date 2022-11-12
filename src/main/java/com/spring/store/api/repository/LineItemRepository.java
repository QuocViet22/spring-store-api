package com.spring.store.api.repository;

import com.spring.store.api.models.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {
    List<LineItem> findByWishListId(Long wishListId);

    @Transactional
    void deleteAllByWishListId(Long wishListId);
}
