package com.ursacore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
public class TestResult {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    private String resultValue;

    @OneToOne(mappedBy = "testResult", fetch = FetchType.LAZY)
    private TestOrderLine testOrderLine;
}