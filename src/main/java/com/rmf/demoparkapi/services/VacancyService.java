package com.rmf.demoparkapi.services;

import com.rmf.demoparkapi.entities.Vacancy;
import com.rmf.demoparkapi.exceptions.AvailableVacancyException;
import com.rmf.demoparkapi.exceptions.CodUniqueViolationException;
import com.rmf.demoparkapi.exceptions.EntityNotFoundException;
import com.rmf.demoparkapi.repositories.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.rmf.demoparkapi.entities.Vacancy.StatusVacancy.FREE;

@RequiredArgsConstructor
@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    @Transactional
    public Vacancy save(Vacancy vacancy) {
        try {
            return vacancyRepository.save(vacancy);
        } catch (DataIntegrityViolationException ex) {
            throw new CodUniqueViolationException("vacancy", vacancy.getCode());
        }
    }

    @Transactional(readOnly = true)
    public Vacancy getByCode(String code) {
        return vacancyRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException("Vacancy", code)
        );
    }

    @Transactional(readOnly = true)
    public Vacancy getByVacancyFree() {
        return vacancyRepository.findFirstByStatus(FREE).orElseThrow(
                () -> new AvailableVacancyException("Nenhuma vaga livre foi encontrada")
        );
    }
}
