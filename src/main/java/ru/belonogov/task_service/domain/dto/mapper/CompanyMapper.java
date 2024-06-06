package ru.belonogov.task_service.domain.dto.mapper;


import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.belonogov.task_service.domain.entity.Employee;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    @Mapping(target = "employees", expression = "java(buildEmployeesToString(company.getEmployees()))")
    CompanyResponse companyToCompanyResponse(Company company);

    @Named("buildEmployeesToString")
    default Set<String> buildEmployeesToString(Set<Employee> employees) {
        if(employees.isEmpty()) {

            return Collections.emptySet();
        }

        return employees.stream()
                .map($ -> ($.getFirstName() + " " + $.getLastName() + " rating : " + $.getRating()))
                .collect(Collectors.toSet());
    }
}
