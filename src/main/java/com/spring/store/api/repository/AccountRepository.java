package com.spring.store.api.repository;

import java.util.Optional;

import com.spring.store.api.models.Account;
import com.spring.store.api.projection.IForgetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query(value = "select a.username as userName, a.password as password, u.email as email\n" +
            "from accounts a join users u on a.id = u.account_id\n" +
            "where a.username = (?1) and u.phone = (?2) and u.email = (?3);", nativeQuery = true)
    IForgetPassword forgetPassword(String userName, String phone, String email);
}
