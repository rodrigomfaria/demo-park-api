package com.rmf.demoparkapi.web.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CostumerResponseDto {
    private Long id;
    private String name;
    private String identificationNumber;
}
