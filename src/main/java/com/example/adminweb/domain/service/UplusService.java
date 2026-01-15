package com.example.adminweb.domain.service;

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

@Entity
@Table(name = "uplus_services")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UplusService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 30)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private Code serviceType;

    @Column
    private Integer price;

    @Column(length = 255)
    private String description;

    @Builder
    private UplusService(String name,
                         String code,
                         Code serviceType,
                         Integer price,
                         String description) {
        this.name = name;
        this.code = code;
        this.serviceType = serviceType;
        this.price = price;
        this.description = description;
    }
}
