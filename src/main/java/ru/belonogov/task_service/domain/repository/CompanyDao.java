package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.CompanyRequest;
import ru.belonogov.task_service.domain.entity.Company;

import java.util.Optional;

public interface CompanyDao {

    Company save(CompanyRequest companyRequest);

    Optional<Company> findById(Long id);

    Company save(Long id, CompanyRequest companyRequest);

    void delete (Long id);


}
