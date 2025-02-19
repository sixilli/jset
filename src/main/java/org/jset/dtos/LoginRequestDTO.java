package org.jset.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public record LoginRequestDTO(
        @Email(message = "email must be valid")
        String email,
        @NotEmpty(message = "password cannot be empty")
        String password
) {}
