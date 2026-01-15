package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.batch.BatchRun;

public interface BatchRunRepository extends JpaRepository<BatchRun, Long> {
}