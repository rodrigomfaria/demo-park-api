package com.rmf.demoparkapi.repositories;

import com.rmf.demoparkapi.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}
