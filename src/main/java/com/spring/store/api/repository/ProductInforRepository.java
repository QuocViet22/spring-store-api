package com.spring.store.api.repository;

import com.spring.store.api.models.ProductInfor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductInforRepository extends JpaRepository<ProductInfor, Long> {
    List<ProductInfor> findByProductId(Long productId);

    Boolean existsBySizeAndProductId(String size, Long productId);

    Optional<ProductInfor> findBySizeAndProductId(String size, Long productId);

}
