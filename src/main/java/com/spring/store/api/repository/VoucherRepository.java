package com.spring.store.api.repository;

import com.spring.store.api.models.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query(value = "select p.*\n" +
            "from vouchers v\n" +
            "where v.name LIKE ?1%", nativeQuery = true)
    List<Voucher> findByName(String name);
}
