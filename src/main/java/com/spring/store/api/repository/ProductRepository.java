package com.spring.store.api.repository;

import com.spring.store.api.models.Product;
import com.spring.store.api.projection.IRecommendProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    @Query(value = "SELECT p.* FROM Products p WHERE p.status = '1'", nativeQuery = true)
    List<Product> getActiveProducts();

    Boolean existsByName(String name);

    @Query(value = "SELECT p.* FROM Products p INNER JOIN Images i ON p.id = i.product_id GROUP BY p.id", nativeQuery = true)
    List<Product> findAllProduct();

    @Query(value = "SELECT p.* FROM Products p " +
            "INNER JOIN Product_infors i ON p.id = i.product_id " +
            "WHERE i.size=(?1);", nativeQuery = true)
    List<Product> findProductBySize(String size);

    @Query(value = "SELECT p.* FROM Products p " +
            "WHERE CAST(p.price as INTEGER)<=(?1);", nativeQuery = true)
    List<Product> findProductByPrice(int price);

    @Query(value = "select p.*\n" +
            "from products p inner join product_infors i on p.id = i.product_id\n" +
            "where CAST(p.price as INTEGER)<=(?1) and i.size=(?2);", nativeQuery = true)
    List<Product> findProductByPriceAndSize(int price, String size);

    @Query(value = "select p.id as id, p.name as name, p.price as price, i.link as link\n" +
            "from images i join products p\n" +
            "on i.product_id = p.id\n" +
            "where p.id=(?1) or p.id=(?2) or p.id=(?3) or p.id=(?4) or p.id=(?5);\n", nativeQuery = true)
    List<IRecommendProduct> recommendProducts(Long no1, Long no2, Long no3, Long no4, Long no5);

}