package ru.belonogov.task_service.servlet.company;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.belonogov.task_service.domain.dto.request.CompanySaveRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.service.CompanyService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;

class CreateCompanyServletTest {

    private CompanyService companyService;
    private Converter converter;
    private CreateCompanyServlet createCompanyServlet;


    @BeforeEach
    void init() throws ServletException {
        createCompanyServlet = new CreateCompanyServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        companyService = mock(CompanyService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("companyService")).thenReturn(companyService);
        createCompanyServlet.init(config);
    }
    @Test
    void TestDoPost_shouldReturnStatus201() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        CompanySaveRequest companySaveRequest = mock(CompanySaveRequest.class);
        CompanyResponse companyResponse = mock(CompanyResponse.class);
        when(converter.getRequestBody(req, CompanySaveRequest.class)).thenReturn(companySaveRequest);
        when(companyService.create(companySaveRequest)).thenReturn(companyResponse);

        createCompanyServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, CompanySaveRequest.class);
        verify(companyService).create(companySaveRequest);
        verify(converter).getResponseBody(resp, companyResponse);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void TestDoPost_shouldReturnStatus400() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        CompanySaveRequest companySaveRequest = mock(CompanySaveRequest.class);
        when(converter.getRequestBody(req, CompanySaveRequest.class)).thenReturn(companySaveRequest);
        when(companyService.create(companySaveRequest)).thenThrow(RuntimeException.class);

        createCompanyServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, CompanySaveRequest.class);
        verify(companyService).create(companySaveRequest);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}