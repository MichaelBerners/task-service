package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.exception.SaveException;
import ru.belonogov.task_service.domain.exception.UpdateException;
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
                throw new SaveException("Ошибка сохранения новой компании");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            company.setId(generatedKeys.getLong("id"));
            company.setEmployees(Collections.emptySet());
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new SaveException("Ошибка сохранения новой компании");
        }

        return company;
    }

    @Override
    public Optional<Company> findById(Long id) {
        String sqlFindById = "select * from employees e right join company c on e.company_id = c.id where c.id = ?";
        Company company = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Employee> employeeHashSet = new HashSet<>();
            while (resultSet.next()) {
                if (company == null) {
                    company = new Company();
                    company.setId(resultSet.getLong(6));
                    company.setName(resultSet.getString("name"));
                    company.setEmployees(employeeHashSet);
                    if(resultSet.getLong(1) == 0) break;
                }
                Employee employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setRating(resultSet.getInt("rating"));
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
        String sqlFindByName = "select * from employees e right join company c on e.company_id = c.id where name = ?";
        Company company = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByName)) {
            preparedStatement.setString(1, companyName);
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Employee> employeeHashSet = new HashSet<>();
            while (resultSet.next()) {
                if(company == null) {
                    company = new Company();
                    company.setId(resultSet.getLong(6));
                    company.setName(resultSet.getString("name"));
                    company.setEmployees(employeeHashSet);
                    if(resultSet.getLong(1) == 0) break;
                }
                Employee employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setRating(resultSet.getInt("rating"));
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
    public Company update(Company company) {
        String sqlUpdate = "update company set name = ? where id = ?";
        String sqlFindById = "select * from employees e right join company c on e.company_id = c.id where c.id = ?";
        String companyName = company.getName();
        Long id = company.getId();
        Company companyUpdate = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement update = connection.prepareStatement(sqlUpdate);
             PreparedStatement findById = connection.prepareStatement(sqlFindById)) {
            update.setString(1, companyName);
            update.setLong(2, id);
            int executeUpdate = update.executeUpdate();
            if (executeUpdate == 0) {
                throw new UpdateException("компания не была изменена");
            }
            Set<Employee> employeeHashSet = new HashSet<>();
            companyUpdate = new Company();
            companyUpdate.setId(id);
            companyUpdate.setName(companyName);
            companyUpdate.setEmployees(employeeHashSet);
            findById.setLong(1, id);
            ResultSet resultSet = findById.executeQuery();
            while (resultSet.next()) {
                if(resultSet.getLong(1) == 0) {
                    break;
                }
                Employee employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setRating(resultSet.getInt("rating"));
                employee.setCompany(companyUpdate);
                employeeHashSet.add(employee);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new UpdateException("компания не была изменена");
        }

        return companyUpdate;
    }

    @Override
    public boolean delete(Long id) {
        String sqlDelete = "delete from company where id = ?";
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
