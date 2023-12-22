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

    String getCostumerIdentificationNumber();

    String getReceiptNumber();

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDhEntry();

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDhExit();

    String getVacancyCode();

    BigDecimal getTotal();

    BigDecimal getDiscount();
}
