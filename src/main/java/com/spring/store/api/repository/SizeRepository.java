package com.spring.store.api.repository;

import com.spring.store.api.models.Product;
import com.spring.store.api.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Optional<Size> findByValue(String value);

    @Query(value = "SELECT p.* FROM Product_sizes p" +
            " WHERE p.product_id=?1", nativeQuery = true)
    List<Product> findProductByProductId(Long productId);
}
