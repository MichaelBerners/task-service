package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;
import ru.belonogov.task_service.domain.exception.SavingTaskException;
import ru.belonogov.task_service.domain.repository.TaskDao;
import ru.belonogov.task_service.util.DataSource;

import java.sql.*;
import java.util.*;

public class TaskDaoImpl implements TaskDao {

    private final Logger logger = LoggerFactory.getLogger(TaskDaoImpl.class);
    private final Set emptySet = Collections.emptySet();

    @Override
    public Task save(Task task) {
        String name = task.getName();
        String description = task.getDescription();
        int rating = task.getRating();
        String taskStatus = task.getTaskStatus().name();
        String sqlInsert = "insert into tasks (name, description, rating, task_status) values (?, ?, ?, ?)";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, rating);
            preparedStatement.setString(4, taskStatus);
            int update = preparedStatement.executeUpdate();
            if (update == 0) {
                throw new SavingTaskException("Ошибка сохранения пользователя");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            task.setId(generatedKeys.getLong("id"));
            task.setEmployees(emptySet);

            return task;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SavingTaskException("Ошибка сохранения пользователя");
        }
    }

    @Override
    public Optional<Task> findById(Long id) {
        String sqlFindById = """
                select t.*, e.*, c.* from tasks_employee te
                join tasks t on te.task_id = t.id
                join employees e on e.id = te.employee_id
                join company c on c.id = e.company_id
                where t.id = ?""";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Task result = null;
            Set<Employee> employeeHashSet = new HashSet<>();
            while (resultSet.next()) {
                if (result == null) {
                    result = new Task();
                    result.setId(resultSet.getLong("t.id"));
                    result.setName(resultSet.getString("t.name"));
                    result.setDescription(resultSet.getString("description"));
                    result.setRating(resultSet.getInt("t.rating"));
                    result.setTaskStatus(TaskStatus.valueOf(resultSet.getString("task_status")));
                    result.setEmployees(employeeHashSet);
                }
                Company company = new Company();
                company.setId(resultSet.getLong("c.id"));
                company.setName(resultSet.getString("c.name"));
                company.setEmployees(emptySet);
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("e.id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setRating(resultSet.getInt("e.rating"));
                employee.setCompany(company);
                employee.setTasks(emptySet);
                employeeHashSet.add(employee);
            }

            return Optional.ofNullable(result);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }

    }

    @Override
    public List<Task> findAllByEmployee(Long id) {
        String sqlFindByEmployee = """
                select t.*, e.* from tasks t
                join tasks_employee te on t.id = te.task_id
                join employees e on te.employee_id = e.id
                where e.id = ? """;
        List<Task> result = new ArrayList<>();

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByEmployee)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getLong("t.id"));
                task.setName(resultSet.getString("name"));
                task.setDescription(resultSet.getString("description"));
                task.setRating(resultSet.getInt("rating"));
                task.setTaskStatus(TaskStatus.valueOf(resultSet.getString("task_status")));
                task.setEmployees(emptySet);
                result.add(task);
            }

            return result;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Task update(Task task) {
        Long id = task.getId();
        String name = task.getName();
        String description = task.getDescription();
        TaskStatus taskStatus = task.getTaskStatus();
        int rating = task.getRating();
        String sqlUpdate = "update tasks set name = ?, description = ?, rating = ?, task_status = ? where id = ?";
        String sqlFindEmployeeByTask = """
                select e.*, c.* from tasks_employee te
                join tasks t on te.task_id = t.id
                join employees e on e.id = te.employee_id
                join company c on c.id = e.company_id         
                where t.id = ?                
                """;
        Task result = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement update = connection.prepareStatement(sqlUpdate);
             PreparedStatement findEmployee = connection.prepareStatement(sqlFindEmployeeByTask)) {
            update.setString(1, name);
            update.setString(2, description);
            update.setInt(3, rating);
            update.setString(4, taskStatus.name());
            update.setLong(5, id);
            int executeUpdate = update.executeUpdate();
            Set<Employee> employeeHashSet = new HashSet<>();
            if (executeUpdate > 0) {
                findEmployee.setLong(1, id);
                ResultSet resultSet = findEmployee.executeQuery();
                while (resultSet.next()) {
                    if (result == null) {
                        result = new Task();
                        result.setId(id);
                        result.setName(name);
                        result.setDescription(description);
                        result.setRating(rating);
                        result.setTaskStatus(taskStatus);
                    }
                    Employee employee = new Employee();
                    Company company = new Company();
                    company.setId(resultSet.getLong("c.id"));
                    company.setName(resultSet.getString("c.name"));
                    company.setEmployees(emptySet);
                    employee.setId(resultSet.getLong("e.id"));
                    employee.setFirstName(resultSet.getString("first_name"));
                    employee.setLastName(resultSet.getString("last_name"));
                    employee.setRating(resultSet.getInt("e.rating"));
                    employee.setCompany(company);
                    employee.setTasks(emptySet);
                    employeeHashSet.add(employee);
                }
                result.setEmployees(employeeHashSet);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean addNewEmployeeToTask(Long taskId, Long employeeId) {
        String sqlAddNewEmployeeToTask = "insert into tasks_employee (task_id, employee_id) values (?, ?)";

        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlAddNewEmployeeToTask)) {
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
        String sqlDelete = "delete from tasks where id = ?";

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
