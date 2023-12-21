package com.rmf.demoparkapi.repositories;

import com.rmf.demoparkapi.entities.Costumer;
import com.rmf.demoparkapi.repositories.projections.CostumerProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CostumerRepository extends JpaRepository<Costumer, Long> {

    @Query("select c from Costumer c")
    Page<CostumerProjection> findAllPageable(Pageable pageable);

    Costumer findByUserId(Long id);

    Optional<Costumer> findByIdentificationNumber(String cpf);
}
