package ru.belonogov.task_service.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @Captor
    ArgumentCaptor<Company> companyArgumentCaptor;



    @Test
    void testCreate_shouldReturnCompanyDTO_whenCompanySaveInDB() {
        CompanySaveRequest companySaveRequest = new CompanySaveRequest();
        companySaveRequest.setName("Gazprom");
        Company company = new Company();
        company.setName(companySaveRequest.getName());
        CompanyResponse companyResponse = new CompanyResponse();
        when(companyDao.save(argThat(arg -> arg.getName().equals(companySaveRequest.getName())))).thenReturn(company);
        when(companyMapper.companyToCompanyResponse(company)).thenReturn(companyResponse);

        CompanyResponse result = companyService.create(companySaveRequest);

        assertThat(result).isEqualTo(companyResponse);
        verify(companyDao).save(any());
        verify(companyMapper).companyToCompanyResponse(any());
    }

    @Test
    void testFindById_shouldReturnCompanyDTO_whenCompanyExist() {
        Long id = 3L;
        Company company = new Company();
        company.setId(id);
        CompanyResponse companyResponse = new CompanyResponse();
        when(companyDao.findById(id)).thenReturn(Optional.of(company));
        when(companyMapper.companyToCompanyResponse(company)).thenReturn(companyResponse);

        CompanyResponse result = companyService.findById(id);

        assertThat(result).isEqualTo(companyResponse);
        verify(companyDao).findById(id);
        verify(companyMapper).companyToCompanyResponse(any());
    }

    @Test
    void testFindById_shouldReturnCompanyNotFoundException_whenCompanyIsNotExist() {
        Long id = 3L;
        when(companyDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> companyService.findById(id));
        verify(companyDao).findById(3L);
    }

    @Test
    void testFindByName_shouldReturnCompanyDTO_whenCompanyExist() {
        String companyName = "Luloil";
        Company company = new Company();
        company.setName(companyName);
        CompanyResponse companyResponse = new CompanyResponse();
        when(companyDao.findByName(companyName)).thenReturn(Optional.of(company));
        when(companyMapper.companyToCompanyResponse(company)).thenReturn(companyResponse);

        CompanyResponse result = companyService.findByName(companyName);

        assertThat(result).isEqualTo(companyResponse);
        verify(companyDao).findByName(companyName);
        verify(companyMapper).companyToCompanyResponse(company);
    }

    @Test
    void testFindByName_shouldReturnCompanyNotFoundException_whenCompanyIsNotExist() {
        String companyName = "Luloil";
        Company company = new Company();
        company.setName(companyName);
        when(companyDao.findByName(companyName)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class,  () -> companyService.findByName(companyName));
    }

    @Test
    void testUpdate_shouldReturnCompanyResponse_whenCompanyUpdate() {
        CompanyUpdateRequest companyUpdateRequest = new CompanyUpdateRequest();
        companyUpdateRequest.setId(1L);
        companyUpdateRequest.setName("TatNeft");
        Company company = new Company();
        company.setId(companyUpdateRequest.getId());
        company.setName(companyUpdateRequest.getName());
        CompanyResponse companyResponse = new CompanyResponse();
        when(companyDao.findById(1L)).thenReturn(Optional.of(company));
        when(companyDao.update(companyArgumentCaptor.capture())).thenReturn(company);
        when(companyMapper.companyToCompanyResponse(company)).thenReturn(companyResponse);

        CompanyResponse result = companyService.update(companyUpdateRequest);

        assertThat(result).isEqualTo(companyResponse);
        assertThat(companyArgumentCaptor.getValue())
                .matches(e -> e.getId().equals(companyUpdateRequest.getId()))
                .matches(e -> e.getName().equals(companyUpdateRequest.getName()));
        verify(companyDao).findById(1L);
        verify(companyDao).update(any());
        verify(companyMapper).companyToCompanyResponse(company);
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
        Company company = new Company();
        company.setId(id);
        when(companyDao.findById(id)).thenReturn(Optional.of(company));

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