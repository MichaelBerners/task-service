package ru.belonogov.task_service.service.impl;

import ru.belonogov.task_service.domain.dto.mapper.CompanyMapper;
import ru.belonogov.task_service.domain.dto.request.CompanyRequest;
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
    public CompanyResponse create(CompanyRequest companyRequest) {
        Company save = companyDao.save(companyRequest);

        return companyMapper.companyToCompanyResponse(save);
    }

    @Override
    public CompanyResponse read(Long id) {
        Company company = companyDao.findById(id).orElseThrow(() -> new CompanyNotFoundException());

        return companyMapper.companyToCompanyResponse(company);
    }

    @Override
    public CompanyResponse read(String name) {
        Company company = companyDao.findByName(name).orElseThrow(() -> new CompanyNotFoundException());

        return companyMapper.companyToCompanyResponse(company);
    }

    @Override
    public CompanyResponse update(Long id, CompanyRequest companyRequest) {
        Company save = companyDao.save(id, companyRequest);

        return companyMapper.companyToCompanyResponse(save);
    }

    @Override
    public void delete(Long id) {
        companyDao.delete(id);

    }
}
