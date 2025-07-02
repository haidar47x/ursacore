package com.ursacore.model;

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
    private Integer version;
    private String sampleCode;
    private SampleType type;
    private LocalDateTime collectedAt;
    private SampleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
