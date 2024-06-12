package ru.belonogov.task_service.servlet.company;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.service.CompanyService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;

class FindCompanyServletTest {

    private FindCompanyServlet findCompanyServlet;
    private Converter converter;
    private CompanyService companyService;

    @BeforeEach
    void init() throws ServletException {
        findCompanyServlet = new FindCompanyServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        companyService = mock(CompanyService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("companyService")).thenReturn(companyService);
        findCompanyServlet.init(config);
    }

    @Test
    void testDoGet_shouldReturnStatus200_whenParamNameExist() throws ServletException, IOException {
        String companyName = "Gazprom";
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        CompanyResponse companyResponse = mock(CompanyResponse.class);
        when(req.getParameter("name")).thenReturn(companyName);
        when(converter.getString(req, "name")).thenReturn(companyName);
        when(companyService.findByName(companyName)).thenReturn(companyResponse);

        findCompanyServlet.doGet(req, resp);

        verify(converter).getString(req, "name");
        verify(companyService).findByName(companyName);
        verify(converter).getResponseBody(resp, companyResponse);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_shouldReturnStatus200_whenParamIdExist() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        CompanyResponse companyResponse = mock(CompanyResponse.class);
        when(req.getParameter("id")).thenReturn("3");
        when(converter.getLong(req, "id")).thenReturn(id);
        when(companyService.findById(id)).thenReturn(companyResponse);

        findCompanyServlet.doGet(req, resp);

        verify(converter).getLong(req, "id");
        verify(companyService).findById(id);
        verify(converter).getResponseBody(resp, companyResponse);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_shouldReturnStatus400() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        CompanyResponse companyResponse = mock(CompanyResponse.class);
        when(req.getParameter("id")).thenReturn("3");
        when(converter.getLong(req, "id")).thenReturn(id);
        when(companyService.findById(id)).thenThrow(RuntimeException.class);

        findCompanyServlet.doGet(req, resp);

        verify(converter).getLong(req, "id");
        verify(companyService).findById(id);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}