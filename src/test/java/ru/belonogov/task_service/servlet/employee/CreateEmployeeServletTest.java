package ru.belonogov.task_service.servlet.employee;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class CreateEmployeeServletTest {

    private CreateEmployeeServlet createEmployeeServlet;
    private Converter converter;
    private EmployeeService employeeService;

    @BeforeEach
    void init() throws ServletException {
        createEmployeeServlet = new CreateEmployeeServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        employeeService = mock(EmployeeService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("employeeService")).thenReturn(employeeService);
        createEmployeeServlet.init(config);
    }

    @Test
    void testDoPost_shouldReturnStatus201() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        EmployeeRequest employeeRequest = mock(EmployeeRequest.class);
        EmployeeResponse employeeResponse = mock(EmployeeResponse.class);
        when(converter.getRequestBody(req, EmployeeRequest.class)).thenReturn(employeeRequest);
        when(employeeService.save(employeeRequest)).thenReturn(employeeResponse);

        createEmployeeServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, EmployeeRequest.class);
        verify(employeeService).save(employeeRequest);
        verify(converter).getResponseBody(resp, employeeResponse);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_CREATED);

    }

    @Test
    void testDoPost_shouldReturnStatus400() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        EmployeeRequest employeeRequest = mock(EmployeeRequest.class);
        EmployeeResponse employeeResponse = mock(EmployeeResponse.class);
        when(converter.getRequestBody(req, EmployeeRequest.class)).thenReturn(employeeRequest);
        when(employeeService.save(employeeRequest)).thenThrow(RuntimeException.class);

        createEmployeeServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, EmployeeRequest.class);
        verify(employeeService).save(employeeRequest);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }
}