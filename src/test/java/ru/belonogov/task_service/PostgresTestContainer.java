package ru.belonogov.task_service;

import org.testcontainers.containers.PostgreSQLContainer;
import ru.belonogov.task_service.util.DataSource;
import ru.belonogov.task_service.util.LiquibaseDemo;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private final static String IMAGE_VERSION = "postgres:14";
    private static PostgresTestContainer postgresTestContainer;

    public PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresTestContainer getInstance() {
        if(postgresTestContainer == null) {
            postgresTestContainer = new PostgresTestContainer()
                    .withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test");
            postgresTestContainer.start();
        }
        return postgresTestContainer;
    }

    @Override
    public void start() {
        super.start();
        DataSource.init(
                postgresTestContainer.getJdbcUrl(),
                postgresTestContainer.getUsername(),
                postgresTestContainer.getPassword(),
                postgresTestContainer.getDriverClassName()
                );
        migration();
    }

    private void migration() {
        try (Connection connection = DataSource.getConnection()){
            LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connection, "db/changelog/db.changelog-master.yaml");
            liquibaseDemo.runMigrations();
        }
        catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
