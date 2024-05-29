package ru.belonogov.task_service.domain.repository.impl;

import ru.belonogov.task_service.domain.dto.request.CompanyRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.repository.CompanyDao;
import ru.belonogov.task_service.util.MyConnectionPool;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class CompanyDaoImpl implements CompanyDao {

    private final MyConnectionPool myConnectionPool = new MyConnectionPool();

    @Override
    public Company save(CompanyRequest companyRequest) {
        Connection connection = null;
        Company result = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement("insert into company (name) values (?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                String companyName = companyRequest.getName();
                preparedStatement.setString(1, companyName);
                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                result = new Company();
                result.setId(generatedKeys.getLong("id"));
                result.setName(companyName);

            }
            return result;
        } catch (SQLException e) {

        } finally {
            if (connection != null) {
                myConnectionPool.release(connection);
            }
        }

        return null;
    }

    @Override
    public Optional<Company> findById(Long id) {
        Company result = null;
        Connection connection = myConnectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from company where id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            result.setId(resultSet.getLong("id"));
            result.setName(resultSet.getString("name"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                myConnectionPool.release(connection);
            }
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Company> findByName(CompanyRequest companyRequest) {
        Company result = null;
        Connection connection = myConnectionPool.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from company where name = ?")) {
            String companyName = companyRequest.getName();
            preparedStatement.setString(1, companyName);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            result.setId(resultSet.getLong("id"));
            result.setName(resultSet.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                myConnectionPool.release(connection);
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Company save(Long id, CompanyRequest companyRequest) {
        Optional<Company> byId = findById(id);
        if(byId.isPresent()) {

        }

        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
