package com.spring.store.api.repository;

import com.spring.store.api.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long productId);

    //    @Query(value = "select i.*\n" +
//            "from images i\n" +
//            "where i.link LIKE CONCAT('%', ?1, '%')\n", nativeQuery = true)
    @Query(value = "SELECT i.*\n" +
            "FROM Images i\n" +
            "WHERE i.product_id=?1\n" +
            "ORDER BY i.id ASC;", nativeQuery = true)
    List<Image> orderByProductId(String link);
}