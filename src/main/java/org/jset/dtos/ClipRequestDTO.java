package org.jset.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public record ClipRequestDTO (
    @NotEmpty
    String clipName,
    @Positive
    int dvrId,
    boolean isHq,
    @Positive
    long startTime,
    @Positive
    long endTime
) {
    public ClipRequestDTO {
        if (startTime >= endTime) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }
}
