package com.ursacore.entity;

import com.ursacore.model.TestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
public class TestCategory {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    @NotNull
    @JdbcTypeCode(SqlTypes.CHAR)
    private TestType type;

    @NotNull
    @Column(length = 64)
    private String categoryName;

    @Column(length= 512)
    private String description;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "test_category_test_order",
        joinColumns = @JoinColumn(name = "test_category_id"),
        inverseJoinColumns = @JoinColumn(name = "test_order_id"))
    private Set<TestOrder> testOrders = new HashSet<>();

    public boolean isNew() {
        return this.type == null;
    }
}
