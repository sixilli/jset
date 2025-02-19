package org.jset.repos;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jooq.DSLContext;
import org.jset.dtos.ClipRequestDTO;
import org.jset.jooq.generated.tables.Clips;
import org.jset.jooq.generated.tables.records.ClipsRecord;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;

import static org.jset.jooq.generated.Tables.CLIPS;


@ApplicationScoped
public class ClipsRepo {
    @Inject
    DSLContext dsl;

    public List<ClipsRecord> getClips() {
        return dsl.select().from(CLIPS).fetch().into(ClipsRecord.class);
    }

    public ClipsRecord getClip(int id) {
        var res = dsl.select()
                .from(CLIPS)
                .where(CLIPS.ID.eq(id))
                .fetchOne();
        if (res == null) throw new NotFoundException("Clip not found with id " + id);
        return res.into(ClipsRecord.class);
    }

    public ClipsRecord create(ClipRequestDTO req) {
        ZoneId zone = ZoneId.systemDefault();
        var startTime = Instant.ofEpochSecond(req.startTime()).atZone(zone).toOffsetDateTime();
        var endTime = Instant.ofEpochSecond(req.endTime()).atZone(zone).toOffsetDateTime();
        var completedAt = Instant.ofEpochSecond(0).atOffset(ZoneOffset.UTC);
        return dsl.insertInto(CLIPS)
            .set(CLIPS.CLIP_NAME, req.clipName())
            .set(CLIPS.FILE_PATH, "")
            .set(CLIPS.DVR_ID, req.dvrId())
            .set(CLIPS.IS_HQ, req.isHq())
            .set(CLIPS.START_TIME, startTime)
            .set(CLIPS.END_TIME, endTime)
            .set(CLIPS.COMPLETED_AT, completedAt)
            .set(CLIPS.CREATED_AT, OffsetDateTime.now())
            .returning()
            .fetchOne();
        // TODO send rabbit message!
    }

    public ClipsRecord clipCompleted(int id, Path filepath) {
        ZoneId zone = ZoneId.systemDefault();
        var completedAt = Instant.now().atZone(zone).toOffsetDateTime();
        return dsl.update(CLIPS)
                .set(CLIPS.FILE_PATH, filepath.toString())
                .set(CLIPS.COMPLETED_AT, completedAt)
                .where(CLIPS.ID.eq(id))
                .returning()
                .fetchOne();
    }
}
