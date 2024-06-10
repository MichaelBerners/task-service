package ru.belonogov.task_service.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapper;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.belonogov.task_service.domain.dto.mapper.CompanyMapper;
import ru.belonogov.task_service.domain.dto.request.CompanySaveRequest;
import ru.belonogov.task_service.domain.dto.request.CompanyUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.entity.Company;
import ru.belonogov.task_service.domain.exception.CompanyNotFoundException;
import ru.belonogov.task_service.domain.exception.UpdateException;
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
    void testCreate_shouldReturnResponseCompanyDTO() {
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
    void testFindByName_shouldReturnCompanyDTO_whenCompanyExist() {
        String companyName = "Luloil";
        Company company = mock(Company.class);
        when(companyDao.findByName(companyName)).thenReturn(Optional.ofNullable(company));
        when(companyMapper.companyToCompanyResponse(company)).thenReturn(mock(CompanyResponse.class));

        companyService.findByName(companyName);

        verify(companyDao).findByName(companyName);
        verify(companyMapper).companyToCompanyResponse(any());
    }

    @Test
    void testFindByName_shouldReturnCompanyNotFoundException_whenCompanyIsNotExist() {
        String companyName = "Luloil";
        Company company = mock(Company.class);
        when(companyDao.findByName(companyName)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class,  () -> companyService.findByName(companyName));
    }

    //@Test
    void testUpdate_shouldReturnCompanyResponse_whenCompanyUpdate() {
        CompanyUpdateRequest companyUpdateRequest = new CompanyUpdateRequest();
        companyUpdateRequest.setId(1L);
        companyUpdateRequest.setName("TatNeft");
        when(companyDao.findById(1L)).thenReturn(mock(Optional.class));
        when(companyDao.update(argThat(arg -> arg.getId() == 1L && arg.getName().equals("TatNeft")))).thenReturn(mock(Company.class));
        when(companyMapper.companyToCompanyResponse(any())).thenReturn(mock(CompanyResponse.class));

        companyService.update(companyUpdateRequest);

        verify(companyDao).findById(1L);
        verify(companyDao).update(any());
        verify(companyMapper).companyToCompanyResponse(any());
    }

    @Test
    void testUpdate_shouldReturnCompanyResponse_whenCompanyIsNotExist() {
        CompanyUpdateRequest companyUpdateRequest = new CompanyUpdateRequest();
        companyUpdateRequest.setId(1L);
        companyUpdateRequest.setName("TatNeft");
        when(companyDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UpdateException.class, () -> companyService.update(companyUpdateRequest));
    }

    @Test
    void testDelete_shouldReturnTrue_whereCompanyExist() {
        Long id = 1L;
        when(companyDao.findById(id)).thenReturn(mock(Optional.class));
        when(companyDao.delete(id)).thenReturn(true);

        companyService.delete(id);

        verify(companyDao).findById(id);
        verify(companyDao).delete(id);
    }

    @Test
    void testDelete_shouldReturnUpdateException_whereCompanyisNotExist() {
        Long id = 1L;
        when(companyDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(UpdateException.class, () -> companyService.delete(id));
    }
}