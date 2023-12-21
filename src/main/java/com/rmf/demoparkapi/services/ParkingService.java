package com.rmf.demoparkapi.services;

import com.rmf.demoparkapi.entities.Costumer;
import com.rmf.demoparkapi.entities.CostumerVacancy;
import com.rmf.demoparkapi.entities.Vacancy;
import com.rmf.demoparkapi.utils.ParkingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ParkingService {

    private final CostumerVacancyService costumerVacancyService;
    private final CostumerService costumerService;
    private final VacancyService vacancyService;

    @Transactional
    public CostumerVacancy checkIn(CostumerVacancy costumerVacancy) {
        Costumer costumer = costumerService.getByIdentificationNumber(costumerVacancy.getCostumer().getIdentificationNumber());
        costumerVacancy.setCostumer(costumer);

        Vacancy vacancy = vacancyService.getByVacancyFree();
        vacancy.setStatus(Vacancy.StatusVacancy.BUSY);
        costumerVacancy.setVacancy(vacancy);

        costumerVacancy.setDhEntry(LocalDateTime.now());

        costumerVacancy.setReceiptNumber(ParkingUtil.generateReceipt());

        return costumerVacancyService.save(costumerVacancy);
    }

    @Transactional
    public CostumerVacancy checkOut(String receipt) {
        CostumerVacancy costumerVacancy = costumerVacancyService.getByReceipt(receipt);

        LocalDateTime dhExit = LocalDateTime.now();

        BigDecimal value = ParkingUtil.calculateCost(costumerVacancy.getDhEntry(), dhExit);
        costumerVacancy.setTotal(value);

        long totalTimes = costumerVacancyService.getTotalTimesFullParking(costumerVacancy.getCostumer().getIdentificationNumber());

        BigDecimal discount = ParkingUtil.calculateDiscount(value, totalTimes);
        costumerVacancy.setDiscount(discount);

        costumerVacancy.setDhExit(dhExit);
        costumerVacancy.getVacancy().setStatus(Vacancy.StatusVacancy.FREE);

        return costumerVacancyService.save(costumerVacancy);
    }
}
