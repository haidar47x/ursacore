package com.ursacore.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TestCategoryDTO {

    private UUID id;
    private Long version;

    @NotNull
    private SampleType sampleType;

    @NotNull
    @Size(max = 64)
    private String categoryName;

    @Size(max = 512)
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
