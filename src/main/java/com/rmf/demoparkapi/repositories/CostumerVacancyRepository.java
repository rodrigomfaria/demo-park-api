package com.rmf.demoparkapi.repositories;

import com.rmf.demoparkapi.entities.CostumerVacancy;
import com.rmf.demoparkapi.repositories.projections.CostumerVacancyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CostumerVacancyRepository extends JpaRepository<CostumerVacancy, Long> {

    Optional<CostumerVacancy> findByReceiptAndDhExitIsNull(String receipt);

    long countByCostumerIdentificationNumberAndDhExitIsNotNull(String identificationNumber);

    Page<CostumerVacancyProjection> findAllByCostumerIdentificationNumber(String identificationNumber, Pageable pageable);

    Page<CostumerVacancyProjection> findAllByCostumerUserId(Long id, Pageable pageable);
}
