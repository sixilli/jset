package org.jset.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jset.dtos.AuthResponseDTO;
import org.jset.dtos.LoginRequestDTO;
import org.jset.dtos.UserDTO;
import org.jset.repos.UserRepo;
import org.jset.services.TokenService;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
    @Inject
    UserRepo userRepo;

    @Inject
    TokenService tokenService;

    @POST
    @Path("/login")
    public Response login(LoginRequestDTO loginRequestDTO) {
        var userData =  userRepo.findByEmail(loginRequestDTO.email());
        if (userData.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        var userDTO = new UserDTO(userData.get());
        String token = tokenService.generateToken(userDTO);
        String refreshToken = tokenService.generateRefreshToken(userDTO);
        return Response.ok(new AuthResponseDTO(token, refreshToken)).build();
    }

    @POST
    @Path("/refresh")
    public Response refresh(@HeaderParam("Authorization") String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = refreshToken.substring(7);

        return tokenService.validateRefreshToken(token)
                .map(email -> userRepo.findByEmail(email)
                        .map(user -> {
                            var userDTO = new UserDTO(user);
                            String newToken = tokenService.generateToken(userDTO);
                            String newRefreshToken = tokenService.generateRefreshToken(userDTO);
                            return Response.ok(new AuthResponseDTO(newToken, newRefreshToken))
                                    .build();
                        })
                        .orElse(Response.status(Response.Status.UNAUTHORIZED).build()))
                .orElse(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String token) {
        // Implement token blacklisting if needed
        return Response.ok().build();
    }
}

