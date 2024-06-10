package ru.belonogov.task_service.servlet.company;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.belonogov.task_service.domain.dto.request.CompanySaveRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.service.CompanyService;
import ru.belonogov.task_service.util.Converter;

import java.io.IOException;

@WebServlet("/company/create")
public class CreateCompanyServlet extends HttpServlet {

    private CompanyService companyService;
    private Converter converter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        companyService = (CompanyService) getServletContext().getAttribute("companyService");
        converter = (Converter) getServletContext().getAttribute("converter");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            CompanySaveRequest requestBody = converter.getRequestBody(req, CompanySaveRequest.class);
            CompanyResponse companyResponse = companyService.create(requestBody);
            converter.getResponseBody(resp, companyResponse);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
