package ru.belonogov.task_service.servlet.tasks;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.service.TaskService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class FindTaskServletTest {

    private FindTaskServlet findTaskServlet;
    private Converter converter;
    private TaskService taskService;

    @BeforeEach
    void init() throws ServletException {
        findTaskServlet = new FindTaskServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        taskService = mock(TaskService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("taskService")).thenReturn(taskService);
        findTaskServlet.init(config);
    }



    @Test
    void testDoGet_shouldReturnStatus200_whenParamIdExist() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        TaskResponse taskResponse = mock(TaskResponse.class);
        when(req.getParameter("id")).thenReturn("3");
        when(converter.getLong(req, "id")).thenReturn(id);
        when(taskService.findById(id)).thenReturn(taskResponse);

        findTaskServlet.doGet(req, resp);

        verify(converter).getLong(req, "id");
        verify(taskService).findById(id);
        verify(converter).getResponseBody(resp, taskResponse);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_shouldReturnStatus200_whenParamEmployeeIdExist() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        List<TaskResponse> taskResponseList = mock(List.class);
        when(req.getParameter("employeeId")).thenReturn("3");
        when(converter.getLong(req, "employeeId")).thenReturn(id);
        when(taskService.findAllByEmployee(id)).thenReturn(taskResponseList);

        findTaskServlet.doGet(req, resp);

        verify(converter).getLong(req, "employeeId");
        verify(taskService).findAllByEmployee(id);
        verify(converter).getResponseBody(resp, taskResponseList);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_shouldReturnStatus400() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        TaskResponse taskResponse = mock(TaskResponse.class);
        when(req.getParameter("id")).thenReturn("3");
        when(converter.getLong(req, "id")).thenReturn(id);
        when(taskService.findById(id)).thenThrow(RuntimeException.class);

        findTaskServlet.doGet(req, resp);

        verify(converter).getLong(req, "id");
        verify(taskService).findById(id);
        verify(resp).setContentType("application/json");
        verify(resp).setCharacterEncoding("UTF-8");
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}