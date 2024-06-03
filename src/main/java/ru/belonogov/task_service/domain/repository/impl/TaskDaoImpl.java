package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.entity.Task;
import ru.belonogov.task_service.domain.entity.TaskStatus;
import ru.belonogov.task_service.domain.repository.TaskDao;
import ru.belonogov.task_service.domain.repository.TaskEmployeeDao;
import ru.belonogov.task_service.util.MyConnectionPool;

import java.sql.*;
import java.util.*;

public class TaskDaoImpl implements TaskDao {

    private final MyConnectionPool myConnectionPool = new MyConnectionPool();
    private final Logger logger = LoggerFactory.getLogger(TaskDaoImpl.class);
    @Override
    public Task save(TaskRequest taskRequest) {
        return null;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByEmployee(Long id) {
        String sqlFindByEmployee = """
                select tasks.*, employee.* tasks t
                join tasks_employee te on t.id = te.task_id
                join employee e on te.employee_id = e.id
                where e.id = ? """;
        Connection connection = null;
        List<Task> result = new ArrayList<>();
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByEmployee)){
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                Employee employee = null;
                while (resultSet.next()) {
                    Task task = new Task();
                    task.setId(resultSet.getLong("id"));
                    task.setName(resultSet.getString("name"));
                    task.setDescription(resultSet.getString("description"));
                    task.setRating(resultSet.getInt("rating"));
                    task.setTaskStatus(TaskStatus.valueOf(resultSet.getString("task_status")));
                    task.setEmployees(Collections.emptyList());

                    result.add(task);
                }
                return result;
            }
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Task update(TaskUpdateRequest taskUpdateRequest) {
        Long id = taskUpdateRequest.getId();
        String name = taskUpdateRequest.getName();
        String description = taskUpdateRequest.getDescription();
        TaskStatus taskStatus = taskUpdateRequest.getTaskStatus();
        int rating = taskUpdateRequest.getRating();
        String sqlUpdate = "update from tasks set name = ?, description = ?, rating = ?, status = ? where id = ?";
        String sqlFindEmployeeByTask = """
                select e.*, c.* tasks t
                join task_employee te on te.tasks_id = t.id
                join employee e on e.id = te.employee_id
                join company c on c.id = e.company_id         
                where t.id = ?                
                """;
        Connection connection = null;
        Task result = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement update = connection.prepareStatement(sqlUpdate);
            PreparedStatement findEmployee = connection.prepareStatement(sqlFindEmployeeByTask)){
                update.setString(1, name);
                update.setString(2, description);
                update.setInt(3, rating);
                update.setString(4, taskStatus.name());
                update.setLong(5, id);
                int executeUpdate = update.executeUpdate();
                List<Employee> employeeList  = new ArrayList<>();
                if(executeUpdate > 0) {
                    findEmployee.setLong(1, id);
                    ResultSet resultSet = findEmployee.executeQuery();
                    while (resultSet.next()) {
                        if(result == null) {
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
                        company.setEmployees(List.of(employee));
                        employee.setId(resultSet.getLong("e.id"));
                        employee.setFirstName(resultSet.getString("e.rirst_name"));
                        employee.setLastName(resultSet.getString("e.last_name"));
                        employee.setRating(resultSet.getInt("e.rating"));
                        employee.setCompany(company);
                        employee.setTasks(List.of(result));
                        employeeList.add(employee);
                    }
                    result.setEmployees(employeeList);
                }
            }
        }
        catch(SQLException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean addNewEmployeeToTask(Long taskId, Long employeeId) {
        String sqlAddNewEmployeeToTask = "insert into tasks_employee (tasks_id, emloyee_id) values (?, ?)";
        Connection connection = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAddNewEmployeeToTask)){
                preparedStatement.setLong(1, taskId);
                preparedStatement.setLong(2, employeeId);
                int i = preparedStatement.executeUpdate();
                if(i > 0) {
                    return true;
                }

                return false;
            }
        }
        catch (SQLException e){
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public void delete(Long id) {
        String sqlDelete = "delete from tasks where id = ?";
        Connection connection = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)){
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }

        }
        catch (SQLException e) {

        }

    }
}
