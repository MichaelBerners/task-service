package ru.belonogov.task_service.servlet.employee;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class DeleteEmployeeServlrtTest {

    private DeleteEmployeeServlrt deleteEmployeeServlrt;
    private Converter converter;
    private EmployeeService employeeService;

    @BeforeEach
    void init() throws ServletException {
        deleteEmployeeServlrt = new DeleteEmployeeServlrt();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        employeeService = mock(EmployeeService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("employeeService")).thenReturn(employeeService);
        deleteEmployeeServlrt.init(config);
    }

    @Test
    void testDoPost_shouldReturnStatus200() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(converter.getLong(req, "id")).thenReturn(id);

        deleteEmployeeServlrt.doPost(req, resp);

        verify(converter).getLong(req, "id");
        verify(employeeService).delete(id);
        verify(resp).setStatus(HttpServletResponse.SC_OK);

    }

    @Test
    void testDoPost_shouldReturnStatus400() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(converter.getLong(req, "id")).thenReturn(id);
        doThrow(RuntimeException.class).when(employeeService).delete(id);

        deleteEmployeeServlrt.doPost(req, resp);

        verify(converter).getLong(req, "id");
        verify(employeeService).delete(id);
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }
}