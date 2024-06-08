package ru.belonogov.task_service.servlet.company;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.belonogov.task_service.domain.dto.request.CompanySaveRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.service.CompanyService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
class CreateCompanyServletTest {

    //@Mock
    private CompanyService companyService;
    //@Mock
    private Converter converter;

    private CreateCompanyServlet createCompanyServlet = new CreateCompanyServlet();


    //@BeforeEach
    void init() {
        converter = mock(Converter.class);
        companyService = mock(CompanyService.class);
    }
    //@Test
    void doPostTest() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        CompanySaveRequest companySaveRequest = mock(CompanySaveRequest.class);
        CompanyResponse companyResponse = mock(CompanyResponse.class);
        //companySaveRequest.setName("Gazprom");

        when(converter.getRequestBody(req, CompanySaveRequest.class)).thenReturn(companySaveRequest);
        when(companyService.create(companySaveRequest)).thenReturn(companyResponse);

        createCompanyServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, CompanySaveRequest.class);
        verify(companyService).create(companySaveRequest);
        verify(converter).getResponseBody(resp, companyResponse);
    }
}