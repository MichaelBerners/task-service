package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.request.CompanyUpdateRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.exception.SaveException;
import ru.belonogov.task_service.domain.repository.CompanyDao;
import ru.belonogov.task_service.util.DataSource;

import java.sql.*;
import java.util.*;

public class CompanyDaoImpl implements CompanyDao {

    private final Logger logger = LoggerFactory.getLogger(CompanyDaoImpl.class);
    private final Set emptySet = Collections.emptySet();

    @Override
    public Company save(Company company) {
        String sqlSave = "insert into company (name) values (?)";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, company.getName());
            int save = preparedStatement.executeUpdate();
            if (save == 0) {
                throw new SaveException("Ошибка сохранения нового пользователя");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            company.setId(generatedKeys.getLong("id"));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SaveException("Ошибка сохранения нового пользователя");
        }

        return company;
    }

    @Override
    public Optional<Company> findById(Long id) {
        String sqlFindById = "select * from employee e join company c on c.company_id = e.id where c.id = ?";
        Company company = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Employee> employeeHashSet = new HashSet<>();
            while (resultSet.next()) {
                if (company == null) {
                    company = new Company();
                    company.setId(resultSet.getLong("c.id"));
                    company.setName(resultSet.getString("c.name"));
                    company.setEmployees(employeeHashSet);
                }
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("e.id"));
                employee.setFirstName(resultSet.getString("e.first_name"));
                employee.setLastName(resultSet.getString("e.last_name"));
                employee.setRating(resultSet.getInt("e.rating"));
                employee.setCompany(company);
                employee.setTasks(emptySet);
                employeeHashSet.add(employee);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());

            return Optional.empty();
        }

        return Optional.ofNullable(company);
    }

    @Override
    public Optional<Company> findByName(String companyName) {
        String sqlFindByName = "select * from employee e join company c on c.company_id = e.id where name = ?";
        Company company = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByName)) {
            preparedStatement.setString(1, companyName);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Employee> employeeHashSet = new HashSet<>();
            while (resultSet.next()) {
                if(company == null) {
                    company = new Company();
                    company.setId(resultSet.getLong("c.id"));
                    company.setName(resultSet.getString("c.name"));
                    company.setEmployees(employeeHashSet);
                }
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("e.id"));
                employee.setFirstName(resultSet.getString("e.first_name"));
                employee.setLastName(resultSet.getString("e.last_name"));
                employee.setRating(resultSet.getInt("e.rating"));
                employee.setCompany(company);
                employeeHashSet.add(employee);

            }
            company.setId(resultSet.getLong("id"));
            company.setName(resultSet.getString("name"));
        } catch (SQLException e) {
            logger.error(e.getMessage());

            return Optional.empty();
        }

        return Optional.ofNullable(company);
    }

    @Override
    public Company update(CompanyUpdateRequest companyUpdateRequest) {
        String sqlUpdate = "update company set name = ? where id = ?";
        String sqlFindById = "select * from employee e join company c on c.company_id = e.id where c.id = ?";
        String companyName = companyUpdateRequest.getName();
        Long id = companyUpdateRequest.getId();
        Company company = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement update = connection.prepareStatement(sqlUpdate);
             PreparedStatement findById = connection.prepareStatement(sqlFindById)) {
            update.setString(1, companyName);
            update.setLong(2, id);
            int executeUpdate = update.executeUpdate();
            if (executeUpdate == 0) {
                throw new RuntimeException("компания не найдена");
            }
            Set<Employee> employeeHashSet = new HashSet<>();
            company = new Company();
            company.setId(id);
            company.setName(companyName);
            company.setEmployees(employeeHashSet);
            ResultSet resultSet = findById.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("e.id"));
                employee.setFirstName(resultSet.getString("e.first_name"));
                employee.setLastName(resultSet.getString("e.last_name"));
                employee.setRating(resultSet.getInt("e.rating"));
                employee.setCompany(company);
                employeeHashSet.add(employee);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return company;
    }

    @Override
    public void delete(Long id) {
        String sqlDelete = "delete from company where id = ?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlDelete)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
