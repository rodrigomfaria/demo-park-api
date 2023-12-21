package com.rmf.demoparkapi.web.dtos.mappers;

import com.rmf.demoparkapi.entities.CostumerVacancy;
import com.rmf.demoparkapi.web.dtos.ParkingCreateDto;
import com.rmf.demoparkapi.web.dtos.ParkingResponseDto;
import org.modelmapper.ModelMapper;

public class CostumerVacancyMapper {

    public static CostumerVacancy toCostumerVacancy(ParkingCreateDto dto) {
        return new ModelMapper().map(dto, CostumerVacancy.class);
    }

    public static ParkingResponseDto toDto(CostumerVacancy costumerVacancy) {
        return new ModelMapper().map(costumerVacancy, ParkingResponseDto.class);
    }
}
