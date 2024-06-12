package ru.belonogov.task_service.servlet.employee;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class FindEmployeeServletTest {

    private FindEmployeeServlet findEmployeeServlet;
    private Converter converter;
    private EmployeeService employeeService;

    @BeforeEach
    void init() throws ServletException {
        findEmployeeServlet = new FindEmployeeServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        employeeService = mock(EmployeeService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("employeeService")).thenReturn(employeeService);
        findEmployeeServlet.init(config);
    }

    @Test
    void testDoGet_shouldReturnStatus200_whenParamTaskNameExist() throws ServletException, IOException {
        String taskName = "TASK_NAME";
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        List<EmployeeResponse> employeeResponseList = mock(List.class);
        when(req.getParameter("task_name")).thenReturn(taskName);
        when(converter.getString(req, "task_name")).thenReturn(taskName);
        when(employeeService.findAllByTask(taskName)).thenReturn(employeeResponseList);

        findEmployeeServlet.doGet(req, resp);

        verify(converter).getString(req, "task_name");
        verify(employeeService).findAllByTask(taskName);
        verify(converter).getResponseBody(resp, employeeResponseList);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_shouldReturnStatus200_whenParamIdExist() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        EmployeeResponse employeeResponse = mock(EmployeeResponse.class);
        when(req.getParameter("id")).thenReturn("3");
        when(converter.getLong(req, "id")).thenReturn(id);
        when(employeeService.findById(id)).thenReturn(employeeResponse);

        findEmployeeServlet.doGet(req, resp);

        verify(converter).getLong(req, "id");
        verify(employeeService).findById(id);
        verify(converter).getResponseBody(resp, employeeResponse);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_shouldReturnStatus400() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        EmployeeResponse employeeResponse = mock(EmployeeResponse.class);
        when(req.getParameter("id")).thenReturn("3");
        when(converter.getLong(req, "id")).thenReturn(id);
        when(employeeService.findById(id)).thenThrow(RuntimeException.class);

        findEmployeeServlet.doGet(req, resp);

        verify(converter).getLong(req, "id");
        verify(employeeService).findById(id);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}