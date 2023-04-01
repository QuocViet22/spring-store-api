package com.spring.store.api.repository;


import com.spring.store.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaRepository<User, Long> {
}
