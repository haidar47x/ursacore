package com.ursacore.entity;

import com.ursacore.model.TestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
public class TestOrder {

    public TestOrder(UUID id, Long version, LocalDateTime createdAt, LocalDateTime updatedAt, TestType testType, String patientRef, Patient patient, Set<TestOrderLine> testOrderLines, Set<TestCategory> categories) {
        this.id = id;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.testType = testType;
        this.patientRef = patientRef;
        this.setPatient(patient);
        this.testOrderLines = testOrderLines;
        this.testCategories = categories;
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

    @NotNull
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private TestType testType;

    @Column
    private String patientRef;

    @ManyToOne
    private Patient patient;

    @OneToMany(mappedBy = "testOrder")
    private Set<TestOrderLine> testOrderLines;

    // Generally, not a very good approach because there is no
    // owning side and an inverse side of the relationship.
    // Considered unconventional, less performant, and a bit confusing.
    // But, it is used here for simplicity.
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "test_category_test_order",
            joinColumns = @JoinColumn(name = "test_order_id"),
            inverseJoinColumns = @JoinColumn(name = "test_category_id"))
    private Set<TestCategory> testCategories = new HashSet<>();

    public void addTestCategory(TestCategory testCategory) {
        this.testCategories.add(testCategory);
        testCategory.getTestOrders().add(this);
    }

    public void removeTestCategory(TestCategory testCategory) {
        this.testCategories.remove(testCategory);
        testCategory.getTestOrders().remove(this);
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        this.patient.getTestOrders().add(this);
    }

    public boolean isNew() {
        return this.id == null;
    }
}
