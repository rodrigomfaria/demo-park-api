package com.rmf.demoparkapi.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacancyResponseDto {
    private Long id;
    private String code;
    private String status;
}
