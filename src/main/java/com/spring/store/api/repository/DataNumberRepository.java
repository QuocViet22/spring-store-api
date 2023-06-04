package com.spring.store.api.repository;

import com.spring.store.api.models.DataNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataNumberRepository extends JpaRepository<DataNumber, Long> {
    DataNumber findByName(String dataNumber);
}
