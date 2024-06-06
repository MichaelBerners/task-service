package ru.belonogov.task_service.servlet.employee;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.belonogov.task_service.domain.dto.request.EmployeeRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

@WebServlet("/employee/create")
public class CreateEmployeeServlet extends HttpServlet {

    private EmployeeService employeeService;
    private Converter converter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        employeeService = (EmployeeService) getServletContext().getAttribute("employeeService");
        converter = (Converter) getServletContext().getAttribute("converter");
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        req.setCharacterEncoding("UTF-8");
        try {
            EmployeeRequest employeeRequest = converter.getRequestBody(req, EmployeeRequest.class);
            EmployeeResponse employeeResponse = employeeService.save(employeeRequest);
            converter.getResponseBody(resp, employeeResponse);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
