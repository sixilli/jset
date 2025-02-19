package org.jset.dtos;

import org.jset.jooq.generated.tables.records.ClipsRecord;

import java.time.OffsetDateTime;

public record ClipResponseDTO(
        int id,
        String clipName,
        String filePath,
        int dvrId,
        boolean isHq,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        OffsetDateTime completedAt
) {
    public ClipResponseDTO(ClipsRecord c) {
        this(c.getId(), c.getClipName(), c.getFilePath(), c.getDvrId(), c.getIsHq(), c.getStartTime(), c.getEndTime(), c.getCompletedAt());
    }
}
