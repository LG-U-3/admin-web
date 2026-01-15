package com.example.adminweb.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.service.UplusService;

public interface UplusServiceRepository extends JpaRepository<UplusService, Long> {
}
