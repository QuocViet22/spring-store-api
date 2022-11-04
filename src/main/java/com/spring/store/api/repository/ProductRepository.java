package com.spring.store.api.repository;

import com.spring.store.api.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    String query =
            "SELECT p.* FROM products p " +
                    "INNER JOIN images i ON products.id=images.product_id" +
                    "WHERE i.product_id=?1 " +
                    "ORDER BY i.id ASC";

    //    @Query(value = query, nativeQuery = true)
    @Query(value = "SELECT p.* FROM Products p INNER JOIN Images i ON p.id = i.product_id GROUP BY p.id", nativeQuery = true)
    List<Product> findAllProduct();
//    @Transactional
//    void deleteByTutorialId(long tutorialId);
}