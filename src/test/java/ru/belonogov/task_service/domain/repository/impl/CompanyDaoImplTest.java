package ru.belonogov.task_service.domain.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.belonogov.task_service.PostgresTestContainer;
import ru.belonogov.task_service.domain.dto.request.CompanyUpdateRequest;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.exception.UpdateException;
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
        assertThat(companyDao.findById(1L).isPresent()).isTrue();
    }

    @Test
    void testFindById_shouldReturnOptional_whenCompanyIsNotExists() {
        assertFalse(companyDao.findById(100L).isPresent());
    }

    @Test
    void testFindByName_shouldReturnOptionalOfCompany_whenCompanyExists() {
        String name = "Gazprom";
        assertTrue(companyDao.findByName(name).isPresent());
    }

    @Test
    void testFindByName_shouldReturnOptional_whenCompanyIsNotExists() {
        String name = "OtherCompany";
        assertFalse(companyDao.findByName(name).isPresent());
    }

    @Test
    void testUpdate_shouldReturnUpdateCompany_whenCompanyExist() {
        Company company = new Company();
        company.setId(2L);
        company.setName("StroyTransGaz");

        Company update = companyDao.update(company);

        assertThat(update)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("name", "StroyTransGaz");
    }

    @Test
    void testUpdate_shouldReturnUpdateException_whenCompanyIsNotExist() {
        Company company = new Company();
        company.setId(100L);
        company.setName("StroyTransGaz");
        assertThrows(UpdateException.class, () -> companyDao.update(company));
    }

    @Test
    void testDelete_shouldReturnTrue_whenCompanyDelete() {
        boolean result = companyDao.delete(4L);
        assertThat(result).isTrue();
    }

    @Test
    void testDelete_shouldReturnFalse_whenCompanyIsNotDelete() {
        boolean result = companyDao.delete(1L);
        assertThat(result).isFalse();
    }

    //@AfterAll
    static void destroy() {
        postgresTestContainer.stop();
    }

}