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
    public Employee save(String firstName, String lastName, int rating, Company company) {
        Connection connection = null;
        Employee result = null;
        String sqlSaveEmployee = "insert into employees (first_name, last_name, rating, company_id) values (?, ?, ?, ?)";

        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSaveEmployee, Statement.RETURN_GENERATED_KEYS);){
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setInt(3, rating);
                preparedStatement.setLong(4, company.getId());
                int i = preparedStatement.executeUpdate();
                if(i == 0) {
                    throw new RuntimeException("Ошибка создания работника");
                }
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                result = new Employee();
                result.setId(generatedKeys.getLong("id"));
                result.setFirstName(firstName);
                result.setLastName(lastName);
                result.setRating(rating);
                result.setCompany(company);
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Ошибка создания работника");
        }
        finally {
            if(connection != null) {
                myConnectionPool.release(connection);
            }
        }

        return result;
    }

    @Override
    public Optional<Employee> findById(Long id) {

        String sqlFindById = "select * from employees as e join company as c on e.company_id = c.id where id = ?";
        Employee result = null;
        Connection connection = null;
        try{
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById) ){
                preparedStatement.setLong(1, id);
                result = getResult(preparedStatement);

                return Optional.ofNullable(result);
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
        finally {
            if(connection != null) {
                myConnectionPool.release(connection);
            }
        }
    }

    @Override
    public Employee update(Long id, int rating) {
        String sqlUpdateRatingById = "update employees set rating = ? where id = ?";
        String sqlFindById = "select * from employees as e join company as c on e.company_id = c.id where id = ?";
        Connection connection = null;
        Employee result = null;

        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement updateRating = connection.prepareStatement(sqlUpdateRatingById);
                 PreparedStatement findById = connection.prepareStatement(sqlFindById)
            ){
                updateRating.setInt(1, rating);
                updateRating.setLong(2, id);
                int quantity = updateRating.executeUpdate();
                if (quantity == 0) {
                    throw new RuntimeException("Работник не найден");
                }
                findById.setLong(1, id);
                result = getResult(findById);
            }
        }
        catch(SQLException e) {
            logger.error(e.getMessage());
        }
        finally {
            if(connection != null) {
                myConnectionPool.release(connection);
            }
        }

        return result;
    }

    private static Employee getResult(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Company company = new Company();
        company.setId(resultSet.getLong("c.id"));
        company.setName(resultSet.getString("name"));
        Employee result = new Employee();
        result.setId(resultSet.getLong("e.id"));
        result.setFirstName(resultSet.getString("first_name"));
        result.setLastName(resultSet.getString("lastName"));
        result.setRating(resultSet.getInt("rating"));
        result.setCompany(company);
        return result;
    }

    @Override
    public void delete(Long id) {
        String sqlDelete = "delete from employees where id = ?";
        Connection connection = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)){
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
        }
        finally {
            if(connection != null) {
                myConnectionPool.release(connection);
            }
        }

    }
}
