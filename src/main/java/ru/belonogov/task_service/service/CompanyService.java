package ru.belonogov.task_service.service;

import ru.belonogov.task_service.domain.dto.request.CompanyRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;

public interface CompanyService {

    CompanyResponse create(CompanyRequest companyRequest);

    CompanyResponse read(Long id);

    CompanyResponse read(CompanyRequest companyRequest);

    CompanyResponse update(Long id, CompanyRequest companyRequest);

    void delete(Long id);

}
