package com.example.adminweb.domain.batch;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_runs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatchRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_type_id", nullable = false)
    private Code batchType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Code status;

    @Column(name = "target_month", nullable = false, length = 7)
    private String targetMonth;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Builder
    private BatchRun(Code batchType,
                     Code status,
                     String targetMonth,
                     LocalDateTime startedAt,
                     LocalDateTime finishedAt) {
        this.batchType = batchType;
        this.status = status;
        this.targetMonth = targetMonth;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }
}
