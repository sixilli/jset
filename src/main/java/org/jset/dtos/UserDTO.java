package org.jset.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jset.jooq.generated.tables.records.UsersRecord;

@RegisterForReflection
public record UserDTO(
        int id,
        String email,
        String role
) {
    public UserDTO(UsersRecord u) {
       this(u.getId(), u.getEmail(), u.getRole());
    }
}