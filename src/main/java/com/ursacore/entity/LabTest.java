package com.ursacore.entity;

import com.ursacore.model.TestCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
public class LabTest {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    @NotNull
    @Column(length = 128, unique = true, nullable = false)
    private String name;

    @NotNull
    @Column(length = 16, unique = true, nullable = false)
    private String code;

    // For tests that require the result to either be
    // positive or negative, we can use 1 for referenceRangeHigh
    // and 0 for referenceRangeLow.
    @NotNull
    private Double referenceRangeHigh;

    @NotNull
    private Double referenceRangeLow;

    private String referenceRangeUnit;

    @Column(length = 1024)
    private String referenceRangeDescription;

    @NotNull
    private Double price;

    @NotNull
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private TestCategory testCategory;

    @Column(length = 512)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /** @Builder.Default
    @ManyToMany
    @JoinTable(name = "test_category_test_order",
        joinColumns = @JoinColumn(name = "test_category_id"),
        inverseJoinColumns = @JoinColumn(name = "test_order_id"))
    private Set<TestOrder> testOrders = new HashSet<>(); */
}
