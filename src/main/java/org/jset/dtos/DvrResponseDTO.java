package org.jset.dtos;

import org.jset.jooq.generated.tables.records.DvrsRecord;

import java.time.OffsetDateTime;

public record DvrResponseDTO (
        int id,
        String name,
        String model,
        String primaryIp,
        String SecondaryIp,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
    ) {
    public DvrResponseDTO(DvrsRecord d) {
        this(d.getId(), d.getName(), d.getModel(), d.getPrimaryIp(), d.getSecondaryIp(), d.getCreatedAt(), d.getUpdatedAt());
    }
}
