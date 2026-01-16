package com.example.adminweb.domain.billing;

import com.example.adminweb.domain.batch.BatchRun;
import com.example.adminweb.domain.code.Code;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "billing_settlements",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_billing_settlements_user_month",
            columnNames = {"user_id", "target_month"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BillingSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_run_id", nullable = false)
    private BatchRun batchRun;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "target_month", nullable = false, length = 7)
    private String targetMonth;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Code status;

    @Builder
    private BillingSettlement(BatchRun batchRun,
                              Long userId,
                              String targetMonth,
                              Integer totalAmount,
                              Code status) {
        this.batchRun = batchRun;
        this.userId = userId;
        this.targetMonth = targetMonth;
        this.totalAmount = totalAmount;
        this.status = status;
    }
}
