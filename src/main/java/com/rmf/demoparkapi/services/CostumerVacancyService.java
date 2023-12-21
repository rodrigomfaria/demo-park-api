package com.rmf.demoparkapi.services;

import com.rmf.demoparkapi.entities.CostumerVacancy;
import com.rmf.demoparkapi.exceptions.EntityNotFoundException;
import com.rmf.demoparkapi.repositories.CostumerVacancyRepository;
import com.rmf.demoparkapi.repositories.projections.CostumerVacancyProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CostumerVacancyService {

    private final CostumerVacancyRepository costumerVacancyRepository;

    @Transactional
    public CostumerVacancy save(CostumerVacancy clienteVaga) {
        return costumerVacancyRepository.save(clienteVaga);
    }

    @Transactional(readOnly = true)
    public CostumerVacancy getByReceipt(String receipt) {
        return costumerVacancyRepository.findByReceiptNumberAndDhExitIsNull(receipt).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Recibo '%s' não encontrado no sistema ou check-out já realizado", receipt)
                )
        );
    }

    @Transactional(readOnly = true)
    public long getTotalTimesFullParking(String identificationNumber) {
        return costumerVacancyRepository.countByCostumerIdentificationNumberAndDhExitIsNotNull(identificationNumber);
    }

    @Transactional(readOnly = true)
    public Page<CostumerVacancyProjection> getAllByCostumerIdentificationNumber(String identificationNumber, Pageable pageable) {
        return costumerVacancyRepository.findAllByCostumerIdentificationNumber(identificationNumber, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CostumerVacancyProjection> getAllByUserId(Long id, Pageable pageable) {
        return costumerVacancyRepository.findAllByCostumerUserId(id, pageable);
    }
}
