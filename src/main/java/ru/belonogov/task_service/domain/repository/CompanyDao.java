package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.CompanyRequest;
import ru.belonogov.task_service.domain.entity.Company;

import java.util.Optional;

public interface CompanyDao {

    Company save(String companyName);

    Optional<Company> findById(Long id);

    Optional<Company> findByName(String companyName);

    Company update(CompanyRequest companyRequest);

    void delete(Long id);


}
