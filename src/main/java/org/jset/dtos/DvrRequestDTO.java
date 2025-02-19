package org.jset.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public record DvrRequestDTO (
    @NotEmpty
    String name,
    @NotEmpty
    String model,
    @NotEmpty
    String primaryIp,

    String secondaryIp
) { }
