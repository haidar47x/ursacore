package com.ursacore.entity;

import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
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
public class Sample {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;

    @NotNull
    @NotBlank
    @Size(max = 4)
    @Column(length = 4)
    private String sampleCode;

    @NotNull
    private SampleType type;

    @NotNull
    private SampleStatus status;

    @NotNull
    private LocalDateTime collectedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
