package org.jset.controllers;

import io.vertx.core.Vertx;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestQuery;
import org.jset.dtos.ClipRequestDTO;
import org.jset.dtos.ClipResponseDTO;
import org.jset.dtos.MultiPartVideoFormDTO;
import org.jset.repos.ClipsRepo;
import org.jset.services.RabbitMQProducer;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Path("/api/clips")
//@RolesAllowed({"User", "Admin"})
public class ClipController {
    private static final Logger LOG = Logger.getLogger(ClipController.class);
    private static final String UPLOAD_DIR = "clips";
    private static final long MAX_SIZE = 1024L * 1024L * 100L; // 100MB

    @Inject
    ClipsRepo repo;

    @Inject
    RabbitMQProducer rabbitMQProducer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClipResponseDTO> getAll() {
        return repo.getClips().stream()
                .map(ClipResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public ClipResponseDTO getById(@PathParam("id") @Min(1) int id) {
        return new ClipResponseDTO(repo.getClip(id));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ClipResponseDTO create(ClipRequestDTO clipRequest) {
        var clip = repo.create(clipRequest);
        rabbitMQProducer.clipRequest(clip);
        return new ClipResponseDTO(clip);
    }


    @POST
    @Path("/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public ClipResponseDTO uploadFile(
            @RestQuery("clip_id") int clipId,
            @RestQuery("camera_id") int cameraId,
            MultiPartVideoFormDTO form
    ) {
        var clip = repo.getClip(clipId);
        var cameraFileName = String.format("%02d", cameraId);

        var filePath = Paths.get(UPLOAD_DIR, Integer.toString(clipId),
                cameraFileName + "_" + clip.getClipName() + ".mp4");

        LOG.info("Streaming clip " + clipId + " to " + filePath + " filename?: " + form.filename);
        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(form.data, filePath, StandardCopyOption.REPLACE_EXISTING);
            form.data.close();
            // TODO checksum!
            LOG.info("we did it");
            var clipRow = repo.clipCompleted(clipId, filePath);
            return new ClipResponseDTO(clipRow);
        } catch (IOException e) {
            LOG.errorf("Failed to handle file upload: %s", e.getMessage());
            throw new WebApplicationException("Failed to process upload", e, 500);
        }
    }
}