package ru.belonogov.task_service.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class LiquibaseDemo {

    private final Connection connection;
    private final String changeLogFile;
    private final Logger logger = LoggerFactory.getLogger(LiquibaseDemo.class);

    public LiquibaseDemo(Connection connection, String changeLogFile) {
        this.connection = connection;
        this.changeLogFile = changeLogFile;
    }

    /**
     * Runs database migrations using Liquibase.
     */
    public void runMigrations() {
        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        }
        catch (LiquibaseException e) {
            logger.error(e.getMessage());
        }
    }
}
