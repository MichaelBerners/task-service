package ru.belonogov.task_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.repository.CompanyDao;
import ru.belonogov.task_service.domain.repository.EmployeeDao;
import ru.belonogov.task_service.domain.repository.TaskDao;
import ru.belonogov.task_service.domain.repository.impl.CompanyDaoImpl;
import ru.belonogov.task_service.domain.repository.impl.EmployeeDaoImpl;
import ru.belonogov.task_service.domain.repository.impl.TaskDaoImpl;
import ru.belonogov.task_service.service.CompanyService;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.service.TaskService;
import ru.belonogov.task_service.service.impl.CompanyServiceImpl;
import ru.belonogov.task_service.service.impl.EmployeeServiceImpl;
import ru.belonogov.task_service.service.impl.TaskServiceImpl;
import ru.belonogov.task_service.util.Converter;
import ru.belonogov.task_service.util.DataSource;
import ru.belonogov.task_service.util.LiquibaseDemo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
@WebListener
public class MyApplicationContextListener implements ServletContextListener {
    private Properties properties;
    private final Logger logger = LoggerFactory.getLogger(MyApplicationContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext servletContext = sce.getServletContext();
        loadProperties(servletContext);
        dataBaseConfiguration(servletContext);
        serviceContextInit(servletContext);

        ServletContextListener.super.contextInitialized(sce);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private void loadProperties(ServletContext servletContext) {
        if(properties == null) {
            properties = new Properties();
        }
        try {
            properties.load(servletContext.getResourceAsStream("/WEB-INF/classes/application.yml"));
            servletContext.setAttribute("servletProperty", properties);
        }
        catch (IOException e) {
            throw new RuntimeException("ошибка чтения конфигурационного файла");
        }
    }

    private void dataBaseConfiguration(ServletContext servletContext){
        String driverClassName = properties.getProperty("db.driver-class-name");
        String url = properties.getProperty("db.url");
        String userName = properties.getProperty("db.userName");
        String password = properties.getProperty("db.password");
        DataSource.init(url, userName, password, driverClassName);
        String changeLogFile = properties.getProperty("liquibase.change-log");
        if (Boolean.parseBoolean(properties.getProperty("liquibase.enabled"))) {
            try (Connection connection = DataSource.getConnection()){
                LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connection, changeLogFile);
                liquibaseDemo.runMigrations();
                servletContext.setAttribute("liquibaseDemo", liquibaseDemo);
            }
            catch (SQLException e) {
                logger.error("Ошибка конфигурирования базы данных");
            }

        }
    }

    private void serviceContextInit(ServletContext servletContext) {
        CompanyDao companyDao = new CompanyDaoImpl();
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        TaskDao taskDao = new TaskDaoImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        Converter converter = new Converter(objectMapper);

        CompanyService companyService = new CompanyServiceImpl(companyDao);
        EmployeeService employeeService = new EmployeeServiceImpl(employeeDao, companyDao);
        TaskService taskService = new TaskServiceImpl(taskDao);
        servletContext.setAttribute("companyService", companyService);
        servletContext.setAttribute("employeeService", employeeService);
        servletContext.setAttribute("taskService", taskService);
        servletContext.setAttribute("converter", converter);
    }
}
