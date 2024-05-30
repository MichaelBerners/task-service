package ru.belonogov.task_service.domain.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.dto.request.CompanyRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.repository.CompanyDao;
import ru.belonogov.task_service.util.MyConnectionPool;

import java.sql.*;
import java.util.Optional;

public class CompanyDaoImpl implements CompanyDao {

    private final Logger logger = LoggerFactory.getLogger(CompanyDaoImpl.class);
    private final MyConnectionPool myConnectionPool = new MyConnectionPool();

    @Override
    public Company save(CompanyRequest companyRequest) {
        String sqlSave = "insert into company (name) values (?)";
        String companyName = companyRequest.getName();
        Connection connection = null;
        Company result = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS)) {
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
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            if (connection != null) {
                myConnectionPool.release(connection);
            }
        }

        return result;
    }

    @Override
    public Optional<Company> findById(Long id) {
        String sqlFindById = "select * from company where id = ?";
        Connection connection = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlFindById)) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                Company result = new Company();
                result.setId(resultSet.getLong("id"));
                result.setName(resultSet.getString("name"));

                return Optional.ofNullable(result);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());

            return Optional.empty();
        } finally {
            if (connection != null) {
                myConnectionPool.release(connection);
            }
        }
    }

    @Override
    public Optional<Company> findByName(CompanyRequest companyRequest) {
        String sqlFindByName = "select * from company where name = ?";
        Connection connection = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlFindByName)) {
                String companyName = companyRequest.getName();
                preparedStatement.setString(1, companyName);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                Company result = new Company();
                result.setId(resultSet.getLong("id"));
                result.setName(resultSet.getString("name"));

                return Optional.ofNullable(result);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());

            return Optional.empty();
        } finally {
            if (connection != null) {
                myConnectionPool.release(connection);
            }
        }
    }

    @Override
    public Company save(Long id, CompanyRequest companyRequest) {
        String sqlUpdate = "update company set name = ? where id = ?";
        String companyName = companyRequest.getName();
        Connection connection = null;
        Company result = null;
        try {
            connection = myConnectionPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate)) {
                preparedStatement.setString(1, companyName);
                preparedStatement.setLong(2, id);
                int i = preparedStatement.executeUpdate();
                if (i == 0) {
                    throw new RuntimeException("пользователь не найден");
                }
                result = new Company();
                result.setId(id);
                result.setName(companyName);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            if (connection != null) {
                myConnectionPool.release(connection);
            }
        }

        return result;
    }

    @Override
    public void delete(Long id) {
        String sqlDelete = "delete from company where id = ?";
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
