package ru.belonogov.task_service.domain.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.belonogov.task_service.PostgresTestContainer;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.repository.CompanyDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompanyDaoImplTest {

    private static CompanyDao companyDao;
    private static PostgresTestContainer postgresTestContainer;

    @BeforeAll
    static void init() {
        PostgresTestContainer instance = PostgresTestContainer.getInstance();
        companyDao = new CompanyDaoImpl();
    }

    @Test
    void testSave_shouldSaveAndReturnCompany() {
        Company company = new Company();
        company.setName("Lukoil");

        Company result = companyDao.save(company);
        System.out.println();

        assertThat(result)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "Lukoil");
    }

    @Test
    void testFindById_shouldReturnOptionalOfCompany_whenCompanyExists() {
        assertTrue(companyDao.findById(1L).isPresent());
    }

    @Test
    void testFindById_shouldReturnOptional_whenCompanyIsNotExists() {
        assertFalse(companyDao.findById(100L).isPresent());
    }

    @Test
    void testFindByName_shouldReturnOptionalOfCompany_whenCompanyExists() {
        assertTrue(companyDao.findByName("Gazprom").isPresent());
    }

    @Test
    void testFindByName_shouldReturnOptional_whenCompanyIsNotExists() {
        assertFalse(companyDao.findByName("OtherCompany").isPresent());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    //@AfterAll
    static void destroy() {
        postgresTestContainer.stop();
    }

}