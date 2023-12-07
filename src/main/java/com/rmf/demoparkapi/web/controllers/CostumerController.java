package com.rmf.demoparkapi.web.controllers;

import com.rmf.demoparkapi.entities.Costumer;
import com.rmf.demoparkapi.jwt.JwtUserDetails;
import com.rmf.demoparkapi.repositories.projections.CostumerProjection;
import com.rmf.demoparkapi.services.CostumerService;
import com.rmf.demoparkapi.services.UserService;
import com.rmf.demoparkapi.web.dtos.CostumerCreateDto;
import com.rmf.demoparkapi.web.dtos.CostumerResponseDto;
import com.rmf.demoparkapi.web.dtos.PageableDto;
import com.rmf.demoparkapi.web.dtos.UserResponseDto;
import com.rmf.demoparkapi.web.dtos.mappers.CostumerMapper;
import com.rmf.demoparkapi.web.dtos.mappers.PageableMapper;
import com.rmf.demoparkapi.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Costumers", description = "Contains all operations related to registering, editing and reading a costumer")
@RestController
@RequestMapping("api/v1/costumers")
@RequiredArgsConstructor
public class CostumerController {

    private final CostumerService costumerService;

    private final UserService userService;

    @Operation(summary = "Create a new costumer",
            description = "feature to create a new costumer",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Identification number already registered in the system",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode = "422",
                            description = "resource not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(
                            responseCode = "403",
                            description = "resource not allowed for ADMIN profile",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('COSTUMER')")
    public ResponseEntity<CostumerResponseDto> create(@Valid @RequestBody CostumerCreateDto costumerCreateDto,
                                                      @AuthenticationPrincipal JwtUserDetails userDetails) {
        Costumer costumer = CostumerMapper.toCostumer(costumerCreateDto);
        costumer.setUser(userService.getById(userDetails.getId()));
        costumerService.save(costumer);
        return ResponseEntity.status(HttpStatus.CREATED).body(CostumerMapper.toDto(costumer));
    }

    @Operation(summary = "Retrieve a costumer by id", description = "Resource to locate a customer by ID. " +
            "Acquisition requires a Bearer Token. Restricted access to ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Without permission to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('COSTUMER')")
    public ResponseEntity<CostumerResponseDto> getById(@PathVariable Long id) {
        Costumer costumer = costumerService.getById(id);
        return ResponseEntity.ok(CostumerMapper.toDto(costumer));
    }

    @Operation(summary = "Retrieve customer list",
            description = "Request requires use of a bearer token. Access restricted to Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Represents the returned page"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Represents the total number of elements per page"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "name,asc")),
                            description = "Represents the ordering of results. Multiple sorting criteria are supported.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = CostumerResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Resource does not allow the CUSTOMER profile",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true)
                                              @PageableDefault(size = 5, sort = {"name"}) Pageable pageable) {
        Page<CostumerProjection> costumers = costumerService.getAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(costumers));
    }

    @Operation(summary = "Retrieve authenticated costumer data",
            description = "\n" +
                    "Request requires use of a bearer token. Restricted access to Role='COSTUMER'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = CostumerResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Resource does not allow the ADMIN profile",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping("/details")
    @PreAuthorize("hasRole('COSTUMER')")
    public ResponseEntity<CostumerResponseDto> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Costumer costumer = costumerService.getByUserId(userDetails.getId());
        return ResponseEntity.ok(CostumerMapper.toDto(costumer));
    }
}
