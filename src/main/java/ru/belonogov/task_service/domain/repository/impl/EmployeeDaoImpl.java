package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;
import ru.belonogov.task_service.domain.exception.SaveException;
import ru.belonogov.task_service.domain.exception.UpdateException;
import ru.belonogov.task_service.domain.repository.EmployeeDao;
import ru.belonogov.task_service.util.DataSource;

import java.sql.*;
import java.util.*;

public class EmployeeDaoImpl implements EmployeeDao {

    private final Logger logger = LoggerFactory.getLogger(EmployeeDaoImpl.class);
    private final Set emptySet = Collections.emptySet();

    @Override
    public Employee save(Employee employee) {
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        int rating = employee.getRating();
        Company company = employee.getCompany();
        String sqlSaveEmployee = "insert into employees (first_name, last_name, rating, company_id) values (?, ?, ?, ?)";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSaveEmployee, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, rating);
            preparedStatement.setLong(4, company.getId());
            int i = preparedStatement.executeUpdate();
            if (i == 0) {
                throw new SaveException("Ошибка создания работника");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            employee.setId(generatedKeys.getLong("id"));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SaveException("Ошибка создания работника");
        }

        return employee;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        String sqlFindById = """
                select * from tasks_employee te
                join employees e on e.id = te.employee_id
                join tasks t on t.id = te.task_id                
                join company c on e.company_id = c.id 
                where id = ?""";
        Employee employee = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Task> taskHashSet = new HashSet<>();
            while (resultSet.next()) {
                if(employee == null) {
                    Company company = new Company();
                    company.setId(resultSet.getLong("c.id"));
                    company.setName(resultSet.getString("c.name"));
                    company.setEmployees(emptySet);
                    employee = new Employee();
                    employee.setId(resultSet.getLong("e.id"));
                    employee.setFirstName(resultSet.getString("first_name"));
                    employee.setLastName(resultSet.getString("lastName"));
                    employee.setRating(resultSet.getInt("e.rating"));
                    employee.setCompany(company);
                    employee.setTasks(taskHashSet);
                }
                Task task = new Task();
                task.setId(resultSet.getLong("t.id"));
                task.setName(resultSet.getString("t.name"));
                task.setDescription(resultSet.getString("description"));
                task.setRating(resultSet.getInt("t.rating"));
                task.setTaskStatus(TaskStatus.valueOf(resultSet.getString("task_status")));
                task.setEmployees(emptySet);
                taskHashSet.add(task);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());

            return Optional.empty();
        }

        return Optional.ofNullable(employee);
    }

    @Override
    public List<Employee> findAllByTask(String taskName) {
        String sqlFindAllByTask = """
                select e.*, c.* from tasks_employee te                
                join employees e on te.employee_id = e.id
                join tasks t on t.id = te.task_id
                join company c on c.id = e.company_id
                where t.name = ?
                """;
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindAllByTask)) {
            preparedStatement.setString(1, taskName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                Company company = new Company();
                company.setId(resultSet.getLong("c.id"));
                company.setName(resultSet.getString("c.name"));
                employee.setId(resultSet.getLong("e.id"));
                employee.setFirstName(resultSet.getString("e.first_name"));
                employee.setLastName(resultSet.getString("e.last_name"));
                employee.setRating(resultSet.getInt("e.rating"));
                employee.setCompany(company);
                employee.setTasks(emptySet);
                employees.add(employee);
            }

        } catch (SQLException e) {
            logger.error("Задачи не найдены");
            return Collections.emptyList();
        }

        return employees;
    }

    @Override
    public Employee update(EmployeeUpdateRequest employeeUpdateRequest) {
        String sqlUpdateRatingById = "update employees set rating = ? where id = ?";
        String sqlFindById = "select * from employees as e join company as c on e.company_id = c.id where id = ?";
        Employee employee = new Employee();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement updateRating = connection.prepareStatement(sqlUpdateRatingById);
             PreparedStatement findById = connection.prepareStatement(sqlFindById)) {
            updateRating.setInt(1, employeeUpdateRequest.getRating());
            updateRating.setLong(2, employeeUpdateRequest.getId());
            int update = updateRating.executeUpdate();
            if (update == 0) {
                throw new UpdateException("Данные работника не обновлены");
            }
            findById.setLong(1, employeeUpdateRequest.getId());
            ResultSet resultSet = findById.executeQuery();
            resultSet.next();
            Company company = new Company();
            company.setId(resultSet.getLong("c.id"));
            company.setName(resultSet.getString("name"));
            employee.setId(resultSet.getLong("e.id"));
            employee.setFirstName(resultSet.getString("first_name"));
            employee.setLastName(resultSet.getString("lastName"));
            employee.setRating(resultSet.getInt("rating"));
            employee.setCompany(company);
            employee.setTasks(Collections.emptySet());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UpdateException("Данные работника не обновлены");
        }

        return employee;

    }

    @Override
    public boolean addNewTask(Long taskId, Long employeeId) {
        String sqlAddNewTask = "insert into tasks_employee (task_id, employee_id) values (?, ?)";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlAddNewTask)) {
            preparedStatement.setLong(1, taskId);
            preparedStatement.setLong(2, employeeId);
            int update = preparedStatement.executeUpdate();
            if (update == 0) {
                return false;
            }

            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage());

            return false;
        }
    }

    @Override
    public void delete(Long id) {
        String sqlDelete = "delete from employees where id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
