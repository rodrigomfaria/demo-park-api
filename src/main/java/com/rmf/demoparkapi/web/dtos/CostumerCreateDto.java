package com.rmf.demoparkapi.web.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CostumerCreateDto {

    @NotNull
    @Size(min = 5, max = 10)
    private String name;

    @Size(min = 5, max = 11)
    @CPF
    private String identificationNumber;
}
