package ru.belonogov.task_service.service;

import ru.belonogov.task_service.domain.dto.request.CompanyRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.entity.Company;

public interface CompanyService {

    CompanyResponse create(String companyName);

    CompanyResponse read(Long id);

    Company read(String companyName);

    CompanyResponse update(CompanyRequest companyRequest);

    void delete(Long id);

}
