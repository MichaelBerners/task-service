package service;

import domain.dto.request.CompanyRequest;
import domain.dto.response.CompanyResponse;
import domain.entity.Company;

public interface CompanyService {

    CompanyResponse create(CompanyRequest companyRequest);

    CompanyResponse read(Long id);

    CompanyResponse update(Long id, CompanyRequest companyRequest);

    void delete(Long id);

}
