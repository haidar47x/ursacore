package com.ursacore.entity;

import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import jakarta.persistence.*;
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
    private String sampleCode;
    private SampleType type;
    private LocalDateTime collectedAt;
    private SampleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
