package ru.belonogov.task_service.servlet.company;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.service.CompanyService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class DeleteCompanyServletTest {

    private DeleteCompanyServlet deleteCompanyServlet;
    private Converter converter;
    private CompanyService companyService;

    @BeforeEach
   void init() throws ServletException {
        deleteCompanyServlet = new DeleteCompanyServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        companyService = mock(CompanyService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("companyService")).thenReturn(companyService);
        deleteCompanyServlet.init(config);
    }

    @Test
    void testDoPost_shouldReturnStatus200() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        Long id = 3L;
        when(converter.getLong(req, "id")).thenReturn(id);

        deleteCompanyServlet.doPost(req, resp);

        verify(converter).getLong(req, "id");
        verify(companyService).delete(id);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost_shouldReturnStatus400() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        Long id = 3L;
        when(converter.getLong(req, "id")).thenReturn(id);
        doThrow(RuntimeException.class).when(companyService).delete(id);

        deleteCompanyServlet.doPost(req, resp);

        verify(converter).getLong(req, "id");
        verify(companyService).delete(id);
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}