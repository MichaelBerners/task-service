package service.impl;

import domain.dto.mapper.CompanyMapper;
import domain.dto.request.CompanyRequest;
import domain.dto.response.CompanyResponse;
import domain.entity.Company;
import domain.exception.CompanyNotFoundException;
import domain.repository.CompanyDao;
import service.CompanyService;

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
    public CompanyResponse update(Long id, CompanyRequest companyRequest) {
        Company save = companyDao.save(id, companyRequest);

        return companyMapper.companyToCompanyResponse(save);
    }

    @Override
    public void delete(Long id) {
        companyDao.delete(id);

    }
}
