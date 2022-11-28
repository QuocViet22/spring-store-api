package com.spring.store.api.repository;

import com.spring.store.api.models.Order;
import com.spring.store.api.projection.IRevenueByDateResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Order, Long> {

    @Query(value =
            "select o.created_date as createdDate, SUM(CAST(o.total_price as int)) as totalPrice, o.status as status, l.product_id as productId, count(l.product_id) as productAmount\n" +
                    "from orders o join line_item_orders l on o.id = l.order_id\n" +
                    "where o.created_date = '26/11/2022' and o.status = '3'\n" +
                    "group by o.created_date, o.status, l.product_id;",
            nativeQuery = true)
    public List<IRevenueByDateResponse> viewRevenueByDate(String modifiedDate);
}
