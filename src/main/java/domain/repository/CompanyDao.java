package domain.repository;

import domain.dto.request.CompanyRequest;
import domain.entity.Company;

import java.util.Optional;

public interface CompanyDao {

    Company save(CompanyRequest companyRequest);

    Optional<Company> findById(Long id);

    Company save(Long id, CompanyRequest companyRequest);

    void delete (Long id);


}
