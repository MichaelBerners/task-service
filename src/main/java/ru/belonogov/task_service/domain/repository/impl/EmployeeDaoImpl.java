package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.mapper.EmployeeMapper;
import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.repository.EmployeeDao;
import ru.belonogov.task_service.util.MyConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoImpl implements EmployeeDao {

    private final MyConnectionPool myConnectionPool = new MyConnectionPool();
    private final Logger logger = LoggerFactory.getLogger(EmployeeDaoImpl.class);

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
                result.setTasks(Collections.emptyList());
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
    public List<Employee> findAllByTask(String taskName) {
        String sqlFindAllByTask = """
                select employee.*, c.*, t.name from employee e
                join company c on c.id = e.company_id
                join tasks_employee te on te.employee_id = e.id
                join task t on t.id = te.task_id where name = ?
                """;
        Connection connection = null;
        List<Employee> employees = new ArrayList<>();
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAllByTask)){
                preparedStatement.setString(1, taskName);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Employee employee = new Employee();
                    Company company = new Company();
                    company.setId(resultSet.getLong("c.id"));
                    company.setName(resultSet.getString("c.name"));
                    company.setEmployees(Collections.emptyList());
                    employee.setId(resultSet.getLong("e.id"));
                    employee.setFirstName(resultSet.getString("e.first_name"));
                    employee.setLastName(resultSet.getString("e.last_name"));
                    employee.setRating(resultSet.getInt("e.rating"));
                    employee.setCompany(company);
                    employee.setTasks(Collections.emptyList());
                    employees.add(employee);
                }
            }
        }
        catch (SQLException e) {
            logger.error("Задачи не найдены");
            return Collections.emptyList();
        }
        finally {
            if(connection != null) {
                myConnectionPool.release(connection);
            }
        }

        return employees;
    }

    @Override
    public Employee update(EmployeeUpdateRequest employeeUpdateRequest) {
        String sqlUpdateRatingById = "update employees set rating = ? where id = ?";
        String sqlFindById = "select * from employees as e join company as c on e.company_id = c.id where id = ?";
        Connection connection = null;
        Employee result = null;

        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement updateRating = connection.prepareStatement(sqlUpdateRatingById);
                 PreparedStatement findById = connection.prepareStatement(sqlFindById)
            ){
                updateRating.setInt(1,employeeUpdateRequest.getRating());
                updateRating.setLong(2, employeeUpdateRequest.getId());
                int quantity = updateRating.executeUpdate();
                if (quantity == 0) {
                    throw new RuntimeException("Работник не найден");
                }
                findById.setLong(1, employeeUpdateRequest.getId());
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

    @Override
    public boolean addNewTask(Long taskId, Long employeeId) {
        String sqlAddNewTask = "insert into tasks_employee (tasks_id, employee_id) values (?, ?)";
        Connection connection = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAddNewTask)){
                preparedStatement.setLong(1, taskId);
                preparedStatement.setLong(2, employeeId);
                int i = preparedStatement.executeUpdate();
                if (i > 0) {
                    return true;
                }

                return false;
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
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
