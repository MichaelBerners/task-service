package ru.belonogov.task_service.domain.dto.mapper;

import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
class CompanyMapperTest {

    CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    @Test
    void companyToCompanyResponse_shouldCompanyDTO_whenEmployeeExist() {
        Company company = new Company();
        Employee employee1 = new Employee();
        employee1.setFirstName("Ivan");
        employee1.setLastName("Ivanov");
        employee1.setRating(5);
        Employee employee2 = new Employee();
        employee2.setFirstName("Vladimir");
        employee2.setLastName("Vladimirov");
        employee2.setRating(5);
        company.setId(1L);
        company.setName("Company");
        company.setEmployees(Set.of(employee1, employee2));
        CompanyResponse companyResponse = companyMapper.companyToCompanyResponse(company);

        assertThat(companyResponse)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .matches(e -> e.getName().equals(company.getName()))
                .matches(e -> e.getEmployees().size() == 2)
                .matches(e -> e.getEmployees().contains("Ivan Ivanov rating : 5"))
                .matches(e -> e.getEmployees().contains("Vladimir Vladimirov rating : 5"));
    }

    @Test
    void companyToCompanyResponse_shouldCompanyDTO_whenEmployeeIsNotExist() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Company");
        company.setEmployees(Collections.emptySet());
        CompanyResponse companyResponse = companyMapper.companyToCompanyResponse(company);

        assertThat(companyResponse)
                .isNotNull()
                .matches(e -> e.getName().equals(company.getName()))
                .matches(e -> e.getEmployees() != null)
                .matches(e -> e.getEmployees().isEmpty());
    }
}