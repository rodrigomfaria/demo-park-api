package com.rmf.demoparkapi.web.controllers;

import com.rmf.demoparkapi.jwt.JwtToken;
import com.rmf.demoparkapi.jwt.JwtUserDetailsService;
import com.rmf.demoparkapi.web.dtos.UserLoginDto;
import com.rmf.demoparkapi.web.dtos.UserResponseDto;
import com.rmf.demoparkapi.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Authenticate to the API", description = "API authentication feature",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful authentication and return of a bearer token",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid field(s)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping(path = "/auth")
    public ResponseEntity<?> authenticate(@RequestBody @Valid UserLoginDto dto, HttpServletRequest request) {
        log.info("Login authentication process: {}", dto.getUsername());

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            JwtToken token = jwtUserDetailsService.getTokenAuthenticated(dto.getUsername());

            return ResponseEntity.ok(token);

        } catch (AuthenticationException ex) {
            log.warn("Bad credentials from username '{}'", dto.getUsername());
        }
        return ResponseEntity
                .badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Invalid credentials!!!"));

    }
}
