package com.rmf.demoparkapi.services;

import com.rmf.demoparkapi.entities.Costumer;
import com.rmf.demoparkapi.exceptions.EntityNotFoundException;
import com.rmf.demoparkapi.exceptions.NumberIdentificationUniqueException;
import com.rmf.demoparkapi.repositories.CostumerRepository;
import com.rmf.demoparkapi.repositories.projections.CostumerProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CostumerService {

    private final CostumerRepository costumerRepository;

    @Transactional
    public Costumer save(Costumer costumer) {
        try {
            return costumerRepository.save(costumer);
        } catch (DataIntegrityViolationException ex) {
            throw new NumberIdentificationUniqueException(
                    String.format("the identification number %s already exists in the system ", costumer.getIdentificationNumber())
            );
        }

    }

    @Transactional(readOnly = true)
    public Costumer getById(Long id) {
        return costumerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Costumer id=%s not found!!!", id))
        );
    }

    @Transactional(readOnly = true)
    public Page<CostumerProjection> getAll(Pageable pageable) {
        return costumerRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Costumer getByUserId(Long id) {
        return costumerRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public Costumer getByIdentificationNumber(String identificationNumber) {
        return costumerRepository.findByIdentificationNumber(identificationNumber).orElseThrow(
                () -> new EntityNotFoundException(String.format("Costumer with identification number '%s' not found", identificationNumber))
        );
    }
}
