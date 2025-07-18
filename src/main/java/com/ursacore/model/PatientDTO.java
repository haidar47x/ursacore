package com.ursacore.model;

import jakarta.validation.constraints.Max;
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

    @NotNull
    @Max(128)
    private Integer age;

    @NotNull
    private Gender gender;

    @NotNull
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
