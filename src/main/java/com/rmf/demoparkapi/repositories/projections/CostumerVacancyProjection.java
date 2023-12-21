package com.rmf.demoparkapi.repositories.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CostumerVacancyProjection {

    String getPlate();

    String getBrand();

    String getModel();

    String getColor();

    String getIdentificationNumber();

    String getReceipt();

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDhEntry();

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDhExit();

    String getVacancyCod();

    BigDecimal getTotal();

    BigDecimal getDiscount();
}
