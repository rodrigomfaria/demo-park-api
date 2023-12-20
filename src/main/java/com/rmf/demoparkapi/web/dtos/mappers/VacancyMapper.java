package com.rmf.demoparkapi.web.dtos.mappers;

import com.rmf.demoparkapi.entities.Vacancy;
import com.rmf.demoparkapi.web.dtos.VacancyCreateDto;
import com.rmf.demoparkapi.web.dtos.VacancyResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacancyMapper {

    public static Vacancy toVacancy(VacancyCreateDto dto) {
        return new ModelMapper().map(dto, Vacancy.class);
    }

    public static VacancyResponseDto toDto(Vacancy vacancy) {
        return new ModelMapper().map(vacancy, VacancyResponseDto.class);
    }
}
