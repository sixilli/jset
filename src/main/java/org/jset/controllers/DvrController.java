package org.jset.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jset.dtos.DvrRequestDTO;
import org.jset.dtos.DvrResponseDTO;
import org.jset.repos.DvrsRepo;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/sites")
public class DvrController {
    private static final Logger LOG = Logger.getLogger(DvrController.class);

    @Inject
    DvrsRepo repo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DvrResponseDTO> getAll() {
        return repo.getAll().stream()
                .map(DvrResponseDTO::new)
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DvrResponseDTO create(DvrRequestDTO dvrRequest) {
        var dvrRow = repo.create(dvrRequest);
        return new DvrResponseDTO(dvrRow);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DvrResponseDTO getOne(@PathParam("id") int id) {
        var dvrRow = repo.getById(id);
        return dvrRow.map(DvrResponseDTO::new).orElseThrow();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
       var isDeleted = repo.deleteById(id);
       if (isDeleted) {
           return Response.ok().build();
       } else {
           return Response.status(Response.Status.NOT_FOUND).build();
       }
    }
}
