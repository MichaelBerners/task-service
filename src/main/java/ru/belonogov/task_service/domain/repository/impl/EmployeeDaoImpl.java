package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.mapper.EmployeeMapper;
import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.repository.EmployeeDao;
import ru.belonogov.task_service.util.MyConnectionPool;

import java.sql.*;
import java.util.Optional;

public class EmployeeDaoImpl implements EmployeeDao {

    private final Company company;
    private final EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;
    private final MyConnectionPool myConnectionPool = new MyConnectionPool();
    private final Logger logger = LoggerFactory.getLogger(EmployeeDaoImpl.class);

    public EmployeeDaoImpl(Company company) {
        this.company = company;
    }

    @Override
    public Employee save(String firstName, String lastName, int rating, Long companyId) {
        Connection connection = null;
        Employee result = null;
        String sqlSaveEmployee = "insert into employee (first_name, last_name, rating, company_id) values (?, ?, ?, ?)";

        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSaveEmployee, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setInt(3, rating);
                preparedStatement.setLong(4, companyId);

                preparedStatement.execute();


            }
        }
        catch (SQLException e) {
            logger.error("");
        }
        finally {
            if(connection != null) {
                myConnectionPool.release(connection);
            }
        }


        return null;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Employee save(Long id, EmployeeRequest companyRequest) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
