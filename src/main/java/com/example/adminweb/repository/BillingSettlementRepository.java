package com.example.adminweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.adminweb.domain.billing.BillingSettlement;

public interface BillingSettlementRepository extends JpaRepository<BillingSettlement, Long> {
}