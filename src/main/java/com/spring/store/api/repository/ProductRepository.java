package com.spring.store.api.repository;

import com.spring.store.api.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    Boolean existsByName(String name);

    @Query(value = "SELECT p.* FROM Products p INNER JOIN Images i ON p.id = i.product_id GROUP BY p.id", nativeQuery = true)
    List<Product> findAllProduct();

    @Query(value = "SELECT p.* FROM Products p " +
            "INNER JOIN Product_infors i ON p.id = i.product_id " +
            "WHERE i.size=(?1) AND CAST(i.amount as INTEGER)>0;", nativeQuery = true)
    List<Product> findProductBySize(String size);

    @Query(value = "SELECT p.* FROM Products p " +
            "WHERE CAST(p.price as INTEGER)<=(?1) AND CAST(i.amount as INTEGER)>0;", nativeQuery = true)
    List<Product> findProductByPrice(int price);

    @Query(value = "select p.*\n" +
            "from products p inner join product_infors i on p.id = i.product_id\n" +
            "where CAST(p.price as INTEGER)<=(?1) and i.size=(?2) AND CAST(i.amount as INTEGER)>0;", nativeQuery = true)
    List<Product> findProductByPriceAndSize(int price, String size);

}