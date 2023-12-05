package com.rmf.demoparkapi.web.controllers;

import com.rmf.demoparkapi.jwt.JwtToken;
import com.rmf.demoparkapi.jwt.JwtUserDetailsService;
import com.rmf.demoparkapi.web.dtos.UserLoginDto;
import com.rmf.demoparkapi.web.exceptions.ErrorMessage;
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
