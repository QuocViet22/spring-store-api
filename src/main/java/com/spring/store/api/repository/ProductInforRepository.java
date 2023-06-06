package com.spring.store.api.repository;

import com.spring.store.api.models.ProductInfor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductInforRepository extends JpaRepository<ProductInfor, Long>, JpaSpecificationExecutor<ProductInfor> {
    List<ProductInfor> findByProductId(Long productId);

    Boolean existsBySizeAndProductId(String size, Long productId);

    Optional<ProductInfor> findBySizeAndProductId(String size, Long productId);

}
