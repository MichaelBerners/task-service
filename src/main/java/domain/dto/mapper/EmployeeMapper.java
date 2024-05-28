package domain.dto.mapper;

import domain.dto.response.EmployeeResponse;
import domain.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "companyName", source = "company.name")
    EmployeeResponse employeeToEmployeeResponse(Employee employee);
}
