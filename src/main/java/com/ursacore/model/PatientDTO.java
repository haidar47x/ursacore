package com.ursacore.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class PatientDTO {

    private UUID id;
    private Integer version;
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
