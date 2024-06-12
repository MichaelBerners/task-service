package ru.belonogov.task_service.servlet.employee;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class UpdateEmployeeServletTest {

    private UpdateEmployeeServlet updateEmployeeServlet;
    private Converter converter;
    private EmployeeService employeeService;

    @BeforeEach
    void init() throws ServletException {
        updateEmployeeServlet = new UpdateEmployeeServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        employeeService = mock(EmployeeService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("employeeService")).thenReturn(employeeService);
        updateEmployeeServlet.init(config);
    }

    @Test
    void testDoPost_shouldReturnStatus200() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        EmployeeUpdateRequest employeeUpdateRequest = mock(EmployeeUpdateRequest.class);
        EmployeeResponse employeeResponse = mock(EmployeeResponse.class);
        when(converter.getRequestBody(req, EmployeeUpdateRequest.class)).thenReturn(employeeUpdateRequest);
        when(employeeService.update(employeeUpdateRequest)).thenReturn(employeeResponse);

        updateEmployeeServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, EmployeeUpdateRequest.class);
        verify(employeeService).update(employeeUpdateRequest);
        verify(converter).getResponseBody(resp, employeeResponse);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_OK);

    }

    @Test
    void testDoPost_shouldReturnStatus400() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        EmployeeUpdateRequest employeeUpdateRequest = mock(EmployeeUpdateRequest.class);
        EmployeeResponse employeeResponse = mock(EmployeeResponse.class);
        when(converter.getRequestBody(req, EmployeeUpdateRequest.class)).thenReturn(employeeUpdateRequest);
        when(employeeService.update(employeeUpdateRequest)).thenThrow(RuntimeException.class);

        updateEmployeeServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, EmployeeUpdateRequest.class);
        verify(employeeService).update(employeeUpdateRequest);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }
}