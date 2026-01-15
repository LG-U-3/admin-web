package com.example.adminweb.domain.charge;

import com.example.adminweb.domain.code.Code;
import com.example.adminweb.domain.service.UplusService;
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

import java.time.LocalDate;

@Entity
@Table(name = "charged_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChargedHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private UplusService service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_type_id", nullable = false)
    private Code chargeType;

    @Column(name = "charge_amount", nullable = false)
    private Integer chargeAmount;

    @Column(name = "charged_at", nullable = false)
    private LocalDate chargedAt;

    @Builder
    private ChargedHistory(Long userId,
                           UplusService service,
                           Code chargeType,
                           Integer chargeAmount,
                           LocalDate chargedAt) {
        this.userId = userId;
        this.service = service;
        this.chargeType = chargeType;
        this.chargeAmount = chargeAmount;
        this.chargedAt = chargedAt;
    }
}
