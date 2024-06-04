package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.request.CompanyRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.entity.Employee;
import ru.belonogov.task_service.domain.repository.CompanyDao;
import ru.belonogov.task_service.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyDaoImpl implements CompanyDao {

    private final Logger logger = LoggerFactory.getLogger(CompanyDaoImpl.class);

    @Override
    public Company save(String companyName) {
        String sqlSave = "insert into company (name) values (?)";
        Company result = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, companyName);
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                result = new Company();
                result.setId(generatedKeys.getLong("id"));
                result.setName(companyName);
            }

            throw new RuntimeException("не удалось сгенерировать id");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

    @Override
    public Optional<Company> findById(Long id) {
        String sqlFindById = "select * from company c join employees e on e.company_id = c.id where id = ?";
        Company result = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Employee> employees = new ArrayList<>();
            while (resultSet.next()) {
                if (result == null) {
                    result = new Company();
                    result.setId(resultSet.getLong("c.id"));
                    result.setName(resultSet.getString("c.name"));
                }
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("e.id"));
                employee.setFirstName(resultSet.getString("e.first_name"));
                employee.setLastName(resultSet.getString("e.last_name"));
                employee.setRating(resultSet.getInt("e.rating"));
                employee.setCompany(result);
                employees.add(employee);
            }

            return Optional.ofNullable(result);
        } catch (SQLException e) {
            logger.error(e.getMessage());

            return Optional.empty();
        }
    }

    @Override
    public Optional<Company> findByName(String companyName) {
        String sqlFindByName = "select * from company where name = ?";
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByName)) {
            preparedStatement.setString(1, companyName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Company result = new Company();
            result.setId(resultSet.getLong("id"));
            result.setName(resultSet.getString("name"));

            return Optional.ofNullable(result);
        } catch (SQLException e) {
            logger.error(e.getMessage());

            return Optional.empty();
        }
    }

    @Override
    public Company update(CompanyRequest companyRequest) {
        String sqlUpdate = "update company set name = ? where id = ?";
        String companyName = companyRequest.getName();
        Long id = companyRequest.getId();
        Company result = null;
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
            preparedStatement.setString(1, companyName);
            preparedStatement.setLong(2, id);
            int i = preparedStatement.executeUpdate();
            if (i == 0) {
                throw new RuntimeException("компания не найдена");
            }
            result = new Company();
            result.setId(id);
            result.setName(companyName);

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return result;
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
