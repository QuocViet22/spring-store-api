package com.spring.store.api.repository;

import com.spring.store.api.models.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends PagingAndSortingRepository<Voucher, Long>, JpaRepository<Voucher, Long> {
    @Query(value = "select v.*\n" +
            "from vouchers v\n" +
            "where v.name LIKE CONCAT('%', ?1,'%');", nativeQuery = true)
    Optional<Voucher> findByName(String name);
}
