package com.rmf.demoparkapi.web.dtos.mappers;

import com.rmf.demoparkapi.entities.Costumer;
import com.rmf.demoparkapi.web.dtos.CostumerCreateDto;
import com.rmf.demoparkapi.web.dtos.CostumerResponseDto;
import org.modelmapper.ModelMapper;

public class CostumerMapper {

    public static Costumer toCostumer(CostumerCreateDto costumerCreateDto) {
        return new ModelMapper().map(costumerCreateDto, Costumer.class);
    }

    public static CostumerResponseDto toDto(Costumer costumer) {
        return new ModelMapper().map(costumer, CostumerResponseDto.class);
    }
}
