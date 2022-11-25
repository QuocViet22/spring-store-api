package com.spring.store.api.repository;

import com.spring.store.api.models.LineItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineItemOrderRepository extends JpaRepository<LineItemOrder, Long> {
    List<LineItemOrder> findByOrderId(Long orderId);
}
