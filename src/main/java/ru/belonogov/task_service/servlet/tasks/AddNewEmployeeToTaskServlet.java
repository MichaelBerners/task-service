package ru.belonogov.task_service.servlet.tasks;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.belonogov.task_service.domain.dto.request.TaskEmployeeRequest;
import ru.belonogov.task_service.service.TaskService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

@WebServlet("/tasks/add_employee")
public class AddNewEmployeeToTaskServlet extends HttpServlet {

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
            TaskEmployeeRequest taskEmployeeRequest = converter.getRequestBody(req, TaskEmployeeRequest.class);
            taskService.addNewEmployeeToTask(taskEmployeeRequest);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}