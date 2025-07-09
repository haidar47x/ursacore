package com.ursacore.entity;

import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sample {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 4)
    @Column(length = 4)
    private String sampleCode;

    @NotNull
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private SampleType type;

    @NotNull
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private SampleStatus status;

    @NotNull
    private LocalDateTime collectedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
