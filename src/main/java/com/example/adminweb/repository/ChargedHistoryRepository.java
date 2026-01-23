package com.example.adminweb.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.charge.ChargedHistory;

public interface ChargedHistoryRepository extends JpaRepository<ChargedHistory, Long> {

//  List<ChargedHistory> findAllByServiceId(Long serviceId);
}
