package com.ursacore.entity;

import com.ursacore.model.BloodType;
import com.ursacore.model.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
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
public class Patient {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
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

    @NotNull
    @Max(128)
    private Integer age;

    @NotNull
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Gender gender;

    @NotNull
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private BloodType bloodType;

    @NotBlank
    @NotNull
    @Size(max = 256)
    private String medicalCondition;

    @Size(max = 128)
    private String doctor;

    @Size(max = 256)
    private String hospital;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
