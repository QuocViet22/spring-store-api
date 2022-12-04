package com.spring.store.api.repository;

import com.spring.store.api.models.Order;
import com.spring.store.api.projection.IRevenueByDateResponse;
import com.spring.store.api.projection.IRevenueByMonthResponse;
import com.spring.store.api.projection.IRevenuePerMonthResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Order, Long> {

    //    revenue by date
    @Query(value =
            "select o.modified_date as modifiedDate, l.total as totalPrice, o.status as status, l.product_id as productId, SUM(CAST(l.amount as int)) as productAmount\n" +
                    "from orders o join line_item_orders l on o.id = l.order_id\n" +
                    "where o.modified_date = ?1 and o.status = '3'\n" +
                    "group by o.modified_date, o.status, l.product_id, l.total\n" +
                    "order by totalPrice desc;\n",
            nativeQuery = true)
    public List<IRevenueByDateResponse> viewRevenueByDate(String modifiedDate);

    //    revenue by month
    @Query(value =
            "select EXTRACT(MONTH FROM TO_DATE(modified_date,'dd/mm/yyyy')) as month, l.total as totalPrice, l.product_id as productId, SUM(CAST(l.amount as int)) as amountProduct\n" +
                    "from orders o join line_item_orders l on o.id = l.order_id\n" +
                    "where o.status = '3' and EXTRACT(MONTH FROM TO_DATE(modified_date,'dd/mm/yyyy')) = ?1 and EXTRACT(year FROM TO_DATE(modified_date,'dd/mm/yyyy')) = ?2\n" +
                    "group by EXTRACT(MONTH FROM TO_DATE(modified_date,'dd/mm/yyyy')), l.product_id, l.total;",
            nativeQuery = true)
    public List<IRevenueByMonthResponse>    viewRevenueByMonth(int month, int year);

    //    top 5 best seller of month
    @Query(value =
            "select EXTRACT(MONTH FROM TO_DATE(modified_date,'dd/mm/yyyy')) as month, l.total as totalPrice, l.product_id as productId, SUM(CAST(l.amount as int)) as amountProduct\n" +
                    "from orders o join line_item_orders l on o.id = l.order_id\n" +
                    "where EXTRACT(MONTH FROM TO_DATE(modified_date,'dd/mm/yyyy')) = ?1 and EXTRACT(year FROM TO_DATE(modified_date,'dd/mm/yyyy')) = ?2 and o.status = '3'\n" +
                    "group by EXTRACT(MONTH FROM TO_DATE(modified_date,'dd/mm/yyyy')), l.product_id, l.total\n" +
                    "order by amountProduct desc\n" +
                    "limit 5;",
            nativeQuery = true)
    public List<IRevenueByMonthResponse> bestSellerOfDate(int month, int year);

    //    revenue by month
    @Query(value =
            "select EXTRACT(month FROM TO_DATE(o.modified_date,'dd/mm/yyyy')) as month, o.total_price as totalPrice\n" +
                    "from orders o join line_item_orders l on o.id = l.order_id\n" +
                    "where EXTRACT(year FROM TO_DATE(modified_date,'dd/mm/yyyy')) = ?1 and o.status = '3'\n" +
                    "group by EXTRACT(month FROM TO_DATE(o.modified_date,'dd/mm/yyyy')), o.total_price;",
            nativeQuery = true)
    public List<IRevenuePerMonthResponse> viewRevenuePerMonth(int year);

}
