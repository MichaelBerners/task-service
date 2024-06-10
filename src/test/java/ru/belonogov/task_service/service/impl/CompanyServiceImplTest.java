package ru.belonogov.task_service.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapper;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.belonogov.task_service.domain.dto.mapper.CompanyMapper;
import ru.belonogov.task_service.domain.dto.request.CompanySaveRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.exception.CompanyNotFoundException;
import ru.belonogov.task_service.domain.repository.CompanyDao;
import ru.belonogov.task_service.service.CompanyService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @InjectMocks
    private CompanyServiceImpl companyService;
    @Mock
    private CompanyDao companyDao;
    @Mock
    CompanyMapper companyMapper;



    @Test
    void testCreate_shouldReturnResponseCompany() {
        CompanySaveRequest companySaveRequest = new CompanySaveRequest();
        companySaveRequest.setName("Gazprom");
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setName(companySaveRequest.getName());
        when(companyDao.save(argThat(arg -> arg.getName().equals(companySaveRequest.getName())))).thenAnswer(e  -> e.getArgument(0));
        when(companyMapper.companyToCompanyResponse(argThat(arg -> arg.getName().equals(companySaveRequest.getName())))).thenReturn(companyResponse);

        companyService.create(companySaveRequest);

        verify(companyDao).save(any());
        verify(companyMapper).companyToCompanyResponse(any());
    }

    @Test
    void testFindById_shouldReturnDTO() {
        Long id = 3L;
        when(companyDao.findById(id)).thenReturn(mock(Optional.class));
        when(companyMapper.companyToCompanyResponse(any())).thenReturn(mock(CompanyResponse.class));

        companyService.findById(id);

        verify(companyDao).findById(id);
        verify(companyMapper).companyToCompanyResponse(any());
    }

    @Test
    void testFindById_shouldReturnCompanyNotFoundException() {
        Long id = 3L;
        when(companyDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> companyService.findById(id));

    }

    @Test
    void findByName() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}