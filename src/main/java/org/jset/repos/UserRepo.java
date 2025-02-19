package org.jset.repos;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.User;
import org.jset.dtos.NewUserRequestDTO;
import org.jset.jooq.generated.tables.Users;
import org.jset.jooq.generated.tables.records.UsersRecord;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class UserRepo {
    @Inject
    DSLContext dsl;

    public Optional<UsersRecord> findByEmail(String email) {
       return Optional.ofNullable(
               dsl.select()
                       .from(Users.USERS)
                       .where(Users.USERS.EMAIL.eq(email))
                       .fetchOneInto(UsersRecord.class));
    }

    public User create(NewUserRequestDTO user) {
        var result = dsl.insertInto(Users.USERS,
                Users.USERS.EMAIL, Users.USERS.PASSWORD, Users.USERS.ROLE, Users.USERS.CREATED_AT)
                .values(user.email(), user.password(), user.role(), OffsetDateTime.now())
                .returning()
                .fetchOne();
        if (result == null) throw new RuntimeException("User already exists");
        return result.into(User.class);
    }
}
