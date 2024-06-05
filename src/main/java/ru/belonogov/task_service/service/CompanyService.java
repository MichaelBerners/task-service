package ru.belonogov.task_service.service;

import ru.belonogov.task_service.domain.dto.request.CompanySaveRequest;
import ru.belonogov.task_service.domain.dto.request.CompanyUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.entity.Company;

public interface CompanyService {

    CompanyResponse create(CompanySaveRequest companySaveRequest);

    CompanyResponse findById(Long id);

    CompanyResponse findByName(String companyName);

    CompanyResponse update(CompanyUpdateRequest companyUpdateRequest);

    void delete(Long id);

}
