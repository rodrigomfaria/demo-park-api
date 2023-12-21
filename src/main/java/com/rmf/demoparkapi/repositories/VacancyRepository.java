package com.rmf.demoparkapi.repositories;

import com.rmf.demoparkapi.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    Optional<Vacancy> findByCode(String cod);
}
