package domain.repository;

import domain.dto.request.CompanyRequest;
import domain.dto.request.EmployeeRequest;
import domain.dto.request.TaskRequest;
import domain.entity.Company;
import domain.entity.Employee;
import domain.entity.Task;

import java.util.Optional;

public interface TaskDao {

    Task save(TaskRequest taskRequest);

    Optional<Task> findById(Long id);

    Task save(Long id, TaskRequest taskRequest);

    void delete (Long id);
}
