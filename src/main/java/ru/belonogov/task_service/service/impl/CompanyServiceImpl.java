package ru.belonogov.task_service.service.impl;

import ru.belonogov.task_service.domain.dto.mapper.CompanyMapper;
import ru.belonogov.task_service.domain.dto.request.CompanySaveRequest;
import ru.belonogov.task_service.domain.dto.request.CompanyUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.exception.CompanyNotFoundException;
import ru.belonogov.task_service.domain.repository.CompanyDao;
import ru.belonogov.task_service.service.CompanyService;

public class CompanyServiceImpl implements CompanyService {

    private final CompanyDao companyDao;
    private final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    public CompanyServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public CompanyResponse create(CompanySaveRequest companySaveRequest) {
        Company company = new Company();
        company.setName(companySaveRequest.getName());
        Company save = companyDao.save(company);

        return companyMapper.companyToCompanyResponse(save);
    }

    @Override
    public CompanyResponse findById(Long id) {
        Company company = companyDao.findById(id).orElseThrow(() -> new CompanyNotFoundException("Компания не найдена"));

        return companyMapper.companyToCompanyResponse(company);
    }

    @Override
    public CompanyResponse findByName(String companyName) {
        Company company = companyDao.findByName(companyName).orElseThrow(() -> new CompanyNotFoundException("Компания не найдена"));

        return companyMapper.companyToCompanyResponse(company);
    }

    @Override
    public CompanyResponse update(CompanyUpdateRequest companyRequest) {
        Company save = companyDao.update(companyRequest);

        return companyMapper.companyToCompanyResponse(save);
    }

    @Override
    public void delete(Long id) {
        companyDao.delete(id);
    }
}
