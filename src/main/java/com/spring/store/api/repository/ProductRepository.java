package com.spring.store.api.repository;

import com.spring.store.api.models.Product;
import com.spring.store.api.projection.IFilterProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    Boolean existsByName(String name);

    String query =
            "SELECT p.* FROM products p " +
                    "INNER JOIN images i ON products.id=images.product_id" +
                    "WHERE i.product_id=?1 " +
                    "ORDER BY i.id ASC";

    //    @Query(value = query, nativeQuery = true)
    @Query(value = "SELECT p.* FROM Products p INNER JOIN Images i ON p.id = i.product_id GROUP BY p.id", nativeQuery = true)
    List<Product> findAllProduct();

    @Query(value = "SELECT p.* FROM Products p " +
            "INNER JOIN Product_infors i ON p.id = i.product_id " +
            "WHERE i.size=(?1)", nativeQuery = true)
    List<Product> findProductBySize(String size);

    @Query(value = "SELECT p.* FROM Products p " +
//            "INNER JOIN Product_infors s ON p.id = s.product_id " +
            "WHERE CAST(p.price as INTEGER)<=(?1)", nativeQuery = true)
    List<Product> findProductByPrice(int price);

    //    @Query(value = "SELECT p.name FROM Products p " +
//            "INNER JOIN Product_infors s ON p.id = s.product_id " +
//            "WHERE s.size=?1 AND p.price=?2", nativeQuery = true)
    @Query(value = "select p.*\n" +
            "from products p inner join product_infors i on p.id = i.product_id\n" +
            "where CAST(p.price as INTEGER)<=(?1) and i.size=(?2);", nativeQuery = true)
//    List<IFilterProductResponse> findProductBySizeAndPrice(String size, String price);
    List<Product> findProductByPriceAndSize(int price, String size);

}