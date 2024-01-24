package com.rmf.demoparkapi.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacancyCreateDto {
    @NotBlank(message = "{NotBlank.vacancyCreateDto.code}")
    @Size(min = 4, max = 4)
    private String code;
    @NotBlank(message = "{NotBlank.vacancyCreateDto.status}")
    @Pattern(regexp = "FREE|BUZY", message = "{Pattern.vacancyCreateDto.status}")
    private String status;
}
