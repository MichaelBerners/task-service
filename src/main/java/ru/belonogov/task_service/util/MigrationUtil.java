package ru.belonogov.task_service.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;

/**
 * класс инкапсулирующий создание миграций бд
 */
public class MigrationUtil {

    private static MigrationUtil INSTANCE = new MigrationUtil();

    private MigrationUtil(){}

    public static final MigrationUtil getInstance() {
        return INSTANCE;
    }

    /**
     * метод создания миграций
     * @param connection соединение с бд
     */
    public void setMigration(Connection connection) {
        try {
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase =
                    new Liquibase("db/changelog/db.changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration is completed successfully");
        }
        catch (LiquibaseException e) {
            System.out.println("Migration Exception : " + e.getMessage());
        }


    }

}
