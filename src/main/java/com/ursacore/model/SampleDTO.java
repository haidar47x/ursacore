package com.ursacore.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SampleDTO {

    private UUID id;

    /**
     * Version for a record is used for optimistic locking during database transactions.
     * It's also used for detecting stale updates on the frontend.
     */
    private Long version;

    @NotBlank
    @NotNull
    @Size(min = 4, max = 4)
    private String sampleCode;

    @NotNull
    private SampleType sampleType;

    @NotNull
    private LocalDateTime collectedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
