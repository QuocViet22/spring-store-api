package com.spring.store.api.repository;

import com.spring.store.api.models.LineItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineItemOrderRepository extends JpaRepository<LineItemOrder, Long> {
}
