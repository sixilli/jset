package org.jset.repos;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.User;
import org.jset.dtos.DvrRequestDTO;
import org.jset.dtos.NewUserRequestDTO;
import org.jset.jooq.generated.tables.Users;
import org.jset.jooq.generated.tables.records.DvrsRecord;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.jset.jooq.generated.Tables.DVRS;

@ApplicationScoped
public class DvrsRepo {
    @Inject
    DSLContext dsl;

    public List<DvrsRecord> getAll() {
        return dsl.select().from(DVRS).fetch().into(DvrsRecord.class);
    }

    public DvrsRecord create(DvrRequestDTO dvr) {
        var secondaryIp = Optional.ofNullable(dvr.secondaryIp()).orElse("");
        var result = dsl.insertInto(DVRS,
                        DVRS.NAME, DVRS.MODEL, DVRS.PRIMARY_IP, DVRS.SECONDARY_IP, DVRS.CREATED_AT)
                .values(dvr.name(), dvr.model(), dvr.primaryIp(), secondaryIp, OffsetDateTime.now())
                .returning()
                .fetchOne();
        if (result == null) throw new RuntimeException("Failed to create dvr");
        return result.into(DvrsRecord.class);
    }

    public Optional<DvrsRecord> getById(int id) {
        var result = dsl.selectFrom(DVRS).where(DVRS.ID.eq(id)).fetchOne();
        if (result == null) return Optional.empty();
        return Optional.of(result.into(DvrsRecord.class));
    }

    public boolean deleteById(int id) {
        var result = dsl.deleteFrom(DVRS).where(DVRS.ID.eq(id)).execute();
        return result > 0;
    }
}
