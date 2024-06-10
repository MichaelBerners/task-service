package ru.belonogov.task_service.servlet.tasks;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.belonogov.task_service.domain.dto.request.TaskRequest;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.service.TaskService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class DeleteTaskServletTest {

    private DeleteTaskServlet deleteTaskServlet;
    private Converter converter;
    private TaskService taskService;

    @BeforeEach
    void init() throws ServletException {
        deleteTaskServlet = new DeleteTaskServlet();
        ServletConfig config = mock(ServletConfig.class);
        ServletContext context = mock(ServletContext.class);
        converter = mock(Converter.class);
        taskService = mock(TaskService.class);
        when(config.getServletContext()).thenReturn(context);
        when(context.getAttribute("converter")).thenReturn(converter);
        when(context.getAttribute("taskService")).thenReturn(taskService);
        deleteTaskServlet.init(config);
    }

    @Test
    void testDoPost_shouldReturnStatus200() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(converter.getLong(req, "id")).thenReturn(id);

        deleteTaskServlet.doPost(req, resp);

        verify(converter).getLong(req, "id");
        verify(taskService).delete(id);
        verify(resp).setStatus(HttpServletResponse.SC_OK);

    }

    @Test
    void testDoPost_shouldReturnStatus400() throws ServletException, IOException {
        Long id = 3L;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(converter.getLong(req, "id")).thenReturn(id);
        doThrow(RuntimeException.class).when(taskService).delete(id);

        deleteTaskServlet.doPost(req, resp);

        verify(converter).getLong(req, "id");
        verify(taskService).delete(id);
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }
}