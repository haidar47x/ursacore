package com.ursacore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;

    // We have both Size and Column length constraints on the value so that
    // we validate the value prior to hitting the database.
    @NotBlank
    @NotNull
    @Size(min = 2, max = 128)
    @Column(length = 128)
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
