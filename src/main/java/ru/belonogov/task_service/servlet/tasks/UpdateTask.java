package ru.belonogov.task_service.servlet.tasks;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.belonogov.task_service.domain.dto.request.TaskUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.service.TaskService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

@WebServlet("/tasks/update")
public class UpdateTask extends HttpServlet {

    private TaskService taskService;
    private Converter converter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        taskService = (TaskService) getServletContext().getAttribute("taskService");
        converter = (Converter) getServletContext().getAttribute("converter");
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            TaskUpdateRequest taskRequest = converter.getRequestBody(req, TaskUpdateRequest.class);
            TaskResponse taskResponse = taskService.update(taskRequest);
            converter.getResponseBody(resp, taskResponse);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
