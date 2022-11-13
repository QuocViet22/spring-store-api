package com.spring.store.api.repository;

import com.spring.store.api.models.LineItem;
import com.spring.store.api.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.Line;
import java.util.List;
import java.util.Optional;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {
    List<LineItem> findByWishListId(Long wishListId);

    boolean existsByProduct(Product product);

    LineItem findByProduct(Product product);

    @Transactional
    void deleteAllByWishListId(Long wishListId);
}
