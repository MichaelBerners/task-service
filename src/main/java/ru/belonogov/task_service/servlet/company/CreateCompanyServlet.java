package ru.belonogov.task_service.servlet.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.belonogov.task_service.domain.dto.request.CompanySaveRequest;
import ru.belonogov.task_service.domain.dto.request.CompanyUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.service.CompanyService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/company/add")
public class CreateCompanyServlet extends HttpServlet {

    private CompanyService companyService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        companyService = (CompanyService) getServletContext().getAttribute("companyService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = req.getReader();
             PrintWriter writer = resp.getWriter()){

            CompanySaveRequest companySaveRequest = objectMapper.readValue(reader, CompanySaveRequest.class);
            CompanyResponse companyResponse = companyService.create(companySaveRequest);
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(writer, companyResponse);
        }
    }
}
