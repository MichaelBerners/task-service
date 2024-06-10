package ru.belonogov.task_service.servlet.employee;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.belonogov.task_service.domain.dto.request.EmployeeUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.EmployeeResponse;
import ru.belonogov.task_service.service.EmployeeService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

@WebServlet("/employee/update")
public class UpdateEmployeeServlet extends HttpServlet {

    private EmployeeService employeeService;
    private Converter converter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        employeeService = (EmployeeService) getServletContext().getAttribute("employeeService");
        converter = (Converter) getServletContext().getAttribute("converter");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            EmployeeUpdateRequest employeeRequest = converter.getRequestBody(req, EmployeeUpdateRequest.class);
            EmployeeResponse employeeResponse = employeeService.update(employeeRequest);
            converter.getResponseBody(resp, employeeResponse);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
