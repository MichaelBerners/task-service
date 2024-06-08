package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;
import ru.belonogov.task_service.domain.exception.SaveException;
import ru.belonogov.task_service.domain.exception.TaskNotFoundException;
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
            employee.setTasks(emptySet);
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
                where e.id = ?""";
        Employee employee = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Task> taskHashSet = new HashSet<>();
            while (resultSet.next()) {
                if(employee == null) {
                    Company company = new Company();
                    company.setId(resultSet.getLong("company_id"));
                    company.setName(resultSet.getString(14));
                    company.setEmployees(emptySet);
                    employee = new Employee();
                    employee.setId(resultSet.getLong("employee_id"));
                    employee.setFirstName(resultSet.getString("first_name"));
                    employee.setLastName(resultSet.getString("last_name"));
                    employee.setRating(resultSet.getInt(6));
                    employee.setCompany(company);
                    employee.setTasks(taskHashSet);
                }
                Long taskId = resultSet.getLong("task_id");
                if(taskId != 0) {
                    Task task = new Task();
                    task.setId(resultSet.getLong("task_id"));
                    task.setName(resultSet.getString(9));
                    task.setDescription(resultSet.getString("description"));
                    task.setRating(resultSet.getInt(11));
                    task.setTaskStatus(TaskStatus.valueOf(resultSet.getString("task_status")));
                    task.setEmployees(emptySet);
                    taskHashSet.add(task);
                }
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
                Long employee_id = resultSet.getLong(1);
                if(employee_id == 0) {
                    break;
                }
                Employee employee = new Employee();
                Company company = new Company();
                company.setId(resultSet.getLong(6));
                company.setName(resultSet.getString("name"));
                company.setEmployees(emptySet);
                employee.setId(employee_id);
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setRating(resultSet.getInt("rating"));
                employee.setCompany(company);
                employee.setTasks(emptySet);
                employees.add(employee);
            }

        } catch (SQLException e) {
            logger.error("Задачи не найдены");
            throw new TaskNotFoundException("Задание не найдено");
        }

        return employees;
    }

    @Override
    public Employee update(Employee employee) {
        String sqlUpdateRatingById = "update employees set rating = ? where id = ?";
        String sqlFindById = "select * from employees e join company c on e.company_id = c.id where e.id = ?";
        Employee employeeUpdate = new Employee();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement updateRating = connection.prepareStatement(sqlUpdateRatingById);
             PreparedStatement findById = connection.prepareStatement(sqlFindById)) {
            updateRating.setInt(1, employee.getRating());
            updateRating.setLong(2, employee.getId());
            int update = updateRating.executeUpdate();
            if (update == 0) {
                throw new UpdateException("Данные работника не обновлены");
            }
            findById.setLong(1, employee.getId());
            ResultSet resultSet = findById.executeQuery();
            resultSet.next();
            Company company = new Company();
            company.setId(resultSet.getLong(5));
            company.setName(resultSet.getString("name"));
            employeeUpdate.setId(resultSet.getLong(1));
            employeeUpdate.setFirstName(resultSet.getString("first_name"));
            employeeUpdate.setLastName(resultSet.getString("last_name"));
            employeeUpdate.setRating(resultSet.getInt("rating"));
            employeeUpdate.setCompany(company);
            employeeUpdate.setTasks(Collections.emptySet());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UpdateException("Данные работника не обновлены");
        }

        return employeeUpdate;

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
    public boolean delete(Long id) {
        String sqlDelete = "delete from employees where id = ?";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }
}
