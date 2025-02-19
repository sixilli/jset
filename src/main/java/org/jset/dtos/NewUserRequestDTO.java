package org.jset.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RegisterForReflection
public record NewUserRequestDTO(
        Long id,
        @NotBlank(message = "Username cannot be blank")
        String username,
        @Email(message = "Email cannot be blank")
        String email,
        @NotBlank(message = "Password cannot be blank")
        String password,
        String role
) {}

