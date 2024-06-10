package ru.belonogov.task_service.servlet.tasks;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.belonogov.task_service.domain.dto.response.TaskResponse;
import ru.belonogov.task_service.service.TaskService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;
import java.util.List;

@WebServlet("/tasks")
public class FindTaskServlet extends HttpServlet {

    private TaskService taskService;
    private Converter converter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        taskService = (TaskService) getServletContext().getAttribute("taskService");
        converter = (Converter) getServletContext().getAttribute("converter");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            if(req.getParameter("id") != null) {
                Long id = converter.getLong(req, "id");
                TaskResponse taskResponse = taskService.findById(id);
                converter.getResponseBody(resp, taskResponse);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else if (req.getParameter("employeeId") != null) {
                Long employeeId = converter.getLong(req, "employeeId");
                List<TaskResponse> tasksResponse = taskService.findAllByEmployee(employeeId);
                converter.getResponseBody(resp, tasksResponse);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
