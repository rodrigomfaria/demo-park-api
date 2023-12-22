package com.rmf.demoparkapi.web.controllers;

import com.rmf.demoparkapi.entities.CostumerVacancy;
import com.rmf.demoparkapi.jwt.JwtUserDetails;
import com.rmf.demoparkapi.repositories.projections.CostumerVacancyProjection;
import com.rmf.demoparkapi.services.CostumerVacancyService;
import com.rmf.demoparkapi.services.ParkingService;
import com.rmf.demoparkapi.web.dtos.PageableDto;
import com.rmf.demoparkapi.web.dtos.ParkingCreateDto;
import com.rmf.demoparkapi.web.dtos.ParkingResponseDto;
import com.rmf.demoparkapi.web.dtos.mappers.CostumerVacancyMapper;
import com.rmf.demoparkapi.web.dtos.mappers.PageableMapper;
import com.rmf.demoparkapi.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Parkings", description = "Operations for registering a vehicle's entry and exit from the parking lot.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/parkings")
public class ParkingController {

    private final ParkingService parkingService;
    private final CostumerVacancyService costumerVacancyService;

    @Operation(summary = "Check-in operation", description = "Resource for entering a vehicle into the parking lot. " +
            "Request requires use of a bearer token. Access restricted to Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL to access the created resource"),
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Possible causes:: <br/>" +
                            "- Customer identification number not registered in the system; <br/>" +
                            "- No free vacancies were found;",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to missing or invalid data",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "I don't allow the feature to the CUSTOMER profile",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDto> checkin(@RequestBody @Valid ParkingCreateDto dto) {
        CostumerVacancy costumerVacancy = CostumerVacancyMapper.toCostumerVacancy(dto);
        parkingService.checkIn(costumerVacancy);
        ParkingResponseDto responseDto = CostumerVacancyMapper.toDto(costumerVacancy);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(costumerVacancy.getReceiptNumber())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    @Operation(summary = "Locate a parked vehicle", description = "Feature for returning a parked vehicle " +
            "by receipt number. Request requires use of a bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = PATH, name = "receipt", description = "Receipt number generated by check-in")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource located successfully",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Receipt number not found.",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/check-in/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COSTUMER')")
    public ResponseEntity<ParkingResponseDto> getByReceipt(@PathVariable String receipt) {
        CostumerVacancy costumerVacancy = costumerVacancyService.getByReceipt(receipt);
        ParkingResponseDto dto = CostumerVacancyMapper.toDto(costumerVacancy);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Check-out operation", description = "Resource for leaving a vehicle from the parking lot. " +
            "Request requires use of a bearer token. Access restricted to Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            parameters = { @Parameter(in = PATH, name = "receipt", description = "Receipt number generated by check-in",
                    required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource updated successfully",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "No receipt number or " +
                            "the vehicle has already been checked out.",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "I don't allow the feature to the CUSTOMER profile",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("/check-out/{receipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDto> checkout(@PathVariable String receipt) {
        CostumerVacancy costumerVacancy = parkingService.checkOut(receipt);
        ParkingResponseDto dto = CostumerVacancyMapper.toDto(costumerVacancy);
        return ResponseEntity.ok(dto);
    }


    @Operation(summary = "Find customer parking records by identification number", description = "Find the " +
            "customer parking records by CPF. Request requires use of a bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = PATH, name = "identification-number", description = "Identification number referring to the costumer to be consulted",
                            required = true
                    ),
                    @Parameter(in = QUERY, name = "page", description = "Represents the returned page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))
                    ),
                    @Parameter(in = QUERY, name = "size", description = "Represents the total number of elements per page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))
                    ),
                    @Parameter(in = QUERY, name = "sort", description = "Default ordering field 'dhEntry,asc'. ",
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dhEntry,asc")),
                            hidden = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource located successfully",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(responseCode = "403", description = "I don't allow the feature to the CUSTOMER profile",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/idenditifcatio-number/{identificationNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAllParkingsByIdentificationNumber(@PathVariable String identificationNumber, @Parameter(hidden = true)
    @PageableDefault(size = 5, sort = "dhEntry",
            direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CostumerVacancyProjection> projection = costumerVacancyService.getAllByCostumerIdentificationNumber(identificationNumber, pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }



    @Operation(summary = "Find logged in customer parking records",
            description = "Locate the logged-in customer's parking records. " +
                    "Request requires use of a bearer token.",
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
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dhEntry,asc")),
                            description = "Standard sort field 'dhEntry,asc'. ")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource located successfully",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "I don't allow the ADMIN profile feature",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('COSTUMER')")
    public ResponseEntity<PageableDto> getAllParkingsCostumer(@AuthenticationPrincipal JwtUserDetails user,
                                                                      @Parameter(hidden = true) @PageableDefault(
                                                                              size = 5, sort = "dhEntry",
                                                                              direction = Sort.Direction.ASC) Pageable pageable) {

        Page<CostumerVacancyProjection> projection = costumerVacancyService.getAllByUserId(user.getId(), pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }
}
