package com.ursacore.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class PatientDTO {

    private UUID id;
    private Integer version;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 128)
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
