package com.example.adminweb.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.charge.ChargedHistory;

public interface ChargedHistoryRepository extends JpaRepository<ChargedHistory, Long> {
}
