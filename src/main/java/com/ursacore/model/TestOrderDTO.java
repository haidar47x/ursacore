package com.ursacore.model;

import com.ursacore.entity.Patient;
import com.ursacore.entity.TestCategory;
import com.ursacore.entity.TestOrderLine;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class TestOrderDTO {

    public TestOrderDTO(UUID id, Long version, LocalDateTime createdAt, LocalDateTime updatedAt, TestType testType, String patientRef, Patient patient, Set<TestOrderLine> testOrderLines, Set<TestCategory> categories) {
        this.id = id;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.testType = testType;
        this.patientRef = patientRef;
        this.setPatient(patient);
    }

    private UUID id;
    private Long version;

    @NotNull
    private TestType testType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Size(max = 255)
    private String patientRef;
    private Patient patient;

    public boolean isNew() {
        return this.id == null;
    }
}
