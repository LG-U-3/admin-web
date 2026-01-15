package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.code.Code;

public interface CodeRepository extends JpaRepository<Code, Long> {
}
