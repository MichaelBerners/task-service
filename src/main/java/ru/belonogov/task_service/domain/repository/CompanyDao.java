package ru.belonogov.task_service.domain.repository;

import ru.belonogov.task_service.domain.dto.request.CompanyUpdateRequest;
import ru.belonogov.task_service.domain.entity.Company;

import java.util.Optional;

public interface CompanyDao {

    Company save(Company company);

    Optional<Company> findById(Long id);

    Optional<Company> findByName(String companyName);

    Company update(Company company);

    boolean delete(Long id);


}
