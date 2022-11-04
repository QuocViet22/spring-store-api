package com.spring.store.api.repository;

import com.spring.store.api.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long productId);

    @Query(value = "SELECT i.* FROM Images i WHERE i.product_id=?1 ORDER BY i.id ASC", nativeQuery = true)
    List<Image> orderByProductId(Long productId);

}
