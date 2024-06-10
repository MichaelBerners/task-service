package ru.belonogov.task_service.servlet.tasks;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.service.TaskService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class AddNewEmployeeToTaskServletTest {

    private AddNewEmployeeToTaskServlet addNewEmployeeToTaskServlet;
    private Converter converter;
    private TaskService taskService;

    @BeforeEach
    void init() throws ServletException {
        addNewEmployeeToTaskServlet = new AddNewEmployeeToTaskServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        taskService = mock(TaskService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("taskService")).thenReturn(taskService);
        addNewEmployeeToTaskServlet.init(config);
    }

    @Test
    void testDoPost_shouldReturnStatus201() throws ServletException, IOException {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        TaskEmployeeRequest taskEmployeeRequest = mock(TaskEmployeeRequest.class);
        when(converter.getRequestBody(req, TaskEmployeeRequest.class)).thenReturn(taskEmployeeRequest);

        addNewEmployeeToTaskServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, TaskEmployeeRequest.class);
        verify(taskService).addNewEmployeeToTask(taskEmployeeRequest);
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
        doThrow(RuntimeException.class).when(taskService).addNewEmployeeToTask(taskEmployeeRequest);

        addNewEmployeeToTaskServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, TaskEmployeeRequest.class);
        verify(taskService).addNewEmployeeToTask(taskEmployeeRequest);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }
}