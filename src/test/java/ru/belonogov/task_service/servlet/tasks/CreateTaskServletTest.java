package ru.belonogov.task_service.servlet.tasks;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.service.TaskService;
import ru.belonogov.task_service.servlet.employee.CreateEmployeeServlet;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class CreateTaskServletTest {

    private CreateTaskServlet createTaskServlet;
    private Converter converter;
    private TaskService taskService;

    @BeforeEach
    void init() throws ServletException {
        createTaskServlet = new CreateTaskServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        taskService = mock(TaskService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("taskService")).thenReturn(taskService);
        createTaskServlet.init(config);
    }

    @Test
    void testDoPost_shouldReturnStatus201() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        TaskRequest taskRequest = mock(TaskRequest.class);
        TaskResponse taskResponse = mock(TaskResponse.class);
        when(converter.getRequestBody(req, TaskRequest.class)).thenReturn(taskRequest);
        when(taskService.save(taskRequest)).thenReturn(taskResponse);

        createTaskServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, TaskRequest.class);
        verify(taskService).save(taskRequest);
        verify(converter).getResponseBody(resp, taskResponse);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_CREATED);

    }

    @Test
    void testDoPost_shouldReturnStatus400() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        TaskRequest taskRequest = mock(TaskRequest.class);
        TaskResponse taskResponse = mock(TaskResponse.class);
        when(converter.getRequestBody(req, TaskRequest.class)).thenReturn(taskRequest);
        when(taskService.save(taskRequest)).thenThrow(RuntimeException.class);

        createTaskServlet.doPost(req, resp);

        verify(converter).getRequestBody(req, TaskRequest.class);
        verify(taskService).save(taskRequest);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }
}