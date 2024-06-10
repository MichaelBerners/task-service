package ru.belonogov.task_service.servlet.company;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.request.CompanyUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.service.CompanyService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class UpdateCompanyServletTest {

    private CompanyService companyService;
    private Converter converter;
    private UpdateCompanyServlet updateCompanyServlet;


    @BeforeEach
    void init() throws ServletException {
        updateCompanyServlet = new UpdateCompanyServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        companyService = mock(CompanyService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("companyService")).thenReturn(companyService);
        updateCompanyServlet.init(config);
    }

    @Test
    void TestDoPost_shouldReturnStatus200() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        CompanyUpdateRequest companyUpdateRequest = mock(CompanyUpdateRequest.class);
        CompanyResponse companyResponse = mock(CompanyResponse.class);
        when(converter.getRequestBody(req, CompanyUpdateRequest.class)).thenReturn(companyUpdateRequest);
        when(companyService.update(companyUpdateRequest)).thenReturn(companyResponse);

        updateCompanyServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, CompanyUpdateRequest.class);
        verify(companyService).update(companyUpdateRequest);
        verify(converter).getResponseBody(resp, companyResponse);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void TestDoPost_shouldReturnStatus400() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        CompanyUpdateRequest companyUpdateRequest = mock(CompanyUpdateRequest.class);
        CompanyResponse companyResponse = mock(CompanyResponse.class);
        when(converter.getRequestBody(req, CompanyUpdateRequest.class)).thenReturn(companyUpdateRequest);
        when(companyService.update(companyUpdateRequest)).thenThrow(RuntimeException.class);

        updateCompanyServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, CompanyUpdateRequest.class);
        verify(companyService).update(companyUpdateRequest);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}