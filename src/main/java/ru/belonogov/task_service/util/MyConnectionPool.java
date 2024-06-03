package ru.belonogov.task_service.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class MyConnectionPool {

    private static List<Connection> connections = Collections.synchronizedList(new LinkedList<>());

    static {
        InputStream inputStream = MyConnectionPool.class.getClassLoader().getResourceAsStream("application.yml");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.userName");
            String password = properties.getProperty("db.password");

            for(int i = 0; i < 10; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                connections.add(connection);
            }
        }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection connection = connections.remove(0);
        System.out.println("receiving connection..." + connection.hashCode());
        System.out.println("remains..." + connections.size());

        return connection;
    }

    public void release(Connection connection) {
        connections.add(connection);
        System.out.println("Put back..." + connection.hashCode());
        System.out.println("The remaining in the pool..." + connections.size());
    }
}
