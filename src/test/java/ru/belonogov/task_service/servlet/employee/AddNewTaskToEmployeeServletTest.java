package ru.belonogov.task_service.servlet.employee;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;

class AddNewTaskToEmployeeServletTest {

    private AddNewTaskToEmployeeServlet addNewTaskToEmployeeServlet;
    private Converter converter;
    private EmployeeService employeeService;

    @BeforeEach
    void init() throws ServletException {
        addNewTaskToEmployeeServlet = new AddNewTaskToEmployeeServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        employeeService = mock(EmployeeService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("employeeService")).thenReturn(employeeService);
        addNewTaskToEmployeeServlet.init(config);
    }

    @Test
    void testDoPost_shouldReturnStatus201() throws ServletException, IOException {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        TaskEmployeeRequest taskEmployeeRequest = mock(TaskEmployeeRequest.class);
        when(converter.getRequestBody(req, TaskEmployeeRequest.class)).thenReturn(taskEmployeeRequest);

        addNewTaskToEmployeeServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, TaskEmployeeRequest.class);
        verify(employeeService).addNewTask(taskEmployeeRequest);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_CREATED);

    }

    @Test
    void testDoPost_shouldReturnStatus400() throws ServletException, IOException {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        TaskEmployeeRequest taskEmployeeRequest = mock(TaskEmployeeRequest.class);
        when(converter.getRequestBody(req, TaskEmployeeRequest.class)).thenReturn(taskEmployeeRequest);
        doThrow(RuntimeException.class).when(employeeService).addNewTask(taskEmployeeRequest);

         addNewTaskToEmployeeServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, TaskEmployeeRequest.class);
        verify(employeeService).addNewTask(taskEmployeeRequest);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }
}