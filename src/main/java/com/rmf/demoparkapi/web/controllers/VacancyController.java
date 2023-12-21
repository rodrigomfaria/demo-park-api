package com.rmf.demoparkapi.web.controllers;

import com.rmf.demoparkapi.entities.Vacancy;
import com.rmf.demoparkapi.services.VacancyService;
import com.rmf.demoparkapi.web.dtos.VacancyCreateDto;
import com.rmf.demoparkapi.web.dtos.VacancyResponseDto;
import com.rmf.demoparkapi.web.dtos.mappers.VacancyMapper;
import com.rmf.demoparkapi.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Vacancy", description = "Contains all operations related to a vacancy resource")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vacancy")
public class VacancyController {

    private final VacancyService vacancyService;

    @Operation(summary = "Create a new vacancy", description = "Resource to create a new vacancy." +
            "Request requires use of a bearer token. Access restricted to Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado")),
                    @ApiResponse(responseCode = "409", description = "URL of the created resource",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to missing or invalid data",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "I don't allow the feature to the CUSTOMER profile",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VacancyCreateDto dto) {
        Vacancy vacancy = VacancyMapper.toVacancy(dto);
        vacancyService.save(vacancy);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(vacancy.getCode())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Find a vacancy", description = "Resource to return a vacancy using your code" +
            "Request requires use of a bearer token. Access restricted to Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource created successfully",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = VacancyResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Vacancy not found",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "I don't allow the feature to the CUSTOMER profile",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VacancyResponseDto> getByCod(@PathVariable String code) {
        Vacancy vacancy = vacancyService.getByCode(code);
        return ResponseEntity.ok(VacancyMapper.toDto(vacancy));
    }
}
