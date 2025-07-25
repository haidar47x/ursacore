package com.ursacore.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TestOrderLineDTO {

    private UUID id;
    private Long version;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
