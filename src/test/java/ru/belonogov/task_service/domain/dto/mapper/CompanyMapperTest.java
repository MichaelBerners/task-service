package ru.belonogov.task_service.domain.dto.mapper;

import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
class CompanyMapperTest {

    CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    @Test
    void companyToCompanyResponse() {

        Company company = new Company();
        Employee employee1 = new Employee();
        employee1.setFirstName("Ivan");
        employee1.setLastName("Ivanov");
        employee1.setRating(5);
        Employee employee2 = new Employee();
        employee1.setFirstName("Vladimir");
        employee1.setLastName("Vladimirov");
        employee1.setRating(5);
        company.setId(1L);
        company.setName("Company");
        company.setEmployees(Set.of(employee1, employee2));
        CompanyResponse companyResponse = companyMapper.companyToCompanyResponse(company);

        assertThat(companyResponse)
                .matches(e -> e.getName().equals(company.getName()))
                .matches(e -> e.getEmployees().size() == 2);

    }

}