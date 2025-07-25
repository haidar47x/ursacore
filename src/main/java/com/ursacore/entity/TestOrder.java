package com.ursacore.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
public class TestOrder {

    public TestOrder(UUID id, Long version, LocalDateTime createdAt, LocalDateTime updatedAt, String patientRef, Patient patient, Set<TestOrderLine> testOrderLines) {
        this.id = id;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.patientRef = patientRef;
        this.setPatient(patient);
        this.testOrderLines = testOrderLines;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column
    private String patientRef;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "testOrder")
    private Set<TestOrderLine> testOrderLines;

    public void setPatient(Patient patient) {
        this.patient = patient;
        this.patient.getTestOrders().add(this);
    }

    public boolean isNew() {
        return this.id == null;
    }
}
