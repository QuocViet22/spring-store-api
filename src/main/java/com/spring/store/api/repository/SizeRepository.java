package com.spring.store.api.repository;

import com.spring.store.api.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Optional<Size> findByValue(String value);

}
