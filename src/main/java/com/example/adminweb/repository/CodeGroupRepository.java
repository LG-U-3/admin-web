package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.code.CodeGroup;

public interface CodeGroupRepository extends JpaRepository<CodeGroup, Long> {
}