package com.ursacore.model;

import com.ursacore.entity.Patient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class TestOrderDTO {

    private UUID id;
    private Long version;

    @NotNull
    private SampleType sampleType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Size(max = 255)
    private String patientRef;
    private Patient patient;

    public boolean isNew() {
        return this.id == null;
    }
}
