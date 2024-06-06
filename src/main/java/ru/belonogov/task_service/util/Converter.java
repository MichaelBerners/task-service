package ru.belonogov.task_service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.belonogov.task_service.domain.exception.UnsupportedMediaTypeException;
import ru.belonogov.task_service.domain.exception.UpdateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Converter {

    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(Converter.class);

    public Converter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T getRequestBody(HttpServletRequest request, Class<T> valueType) {
        T result = null;
        if (!request.getContentType().equals("application/json")) {
            throw new UnsupportedMediaTypeException("не поддерживаемый тип");
        }
        try (BufferedReader reader = request.getReader()) {
            result = objectMapper.readValue(reader, valueType);
        } catch (IOException e) {
            throw new UpdateException("");
        }

        return result;
    }

    public void getResponseBody(HttpServletResponse response, Object ob) {
        try (PrintWriter writer = response.getWriter()){
            objectMapper.writeValue(writer, ob);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(HttpServletRequest request, String parametrName) {

        return request.getParameter(parametrName);
    }

    public Long getLong(HttpServletRequest request, String parameterName) {
        Long result = 0L;
        try {
            result = Long.parseLong(request.getParameter(parameterName));
        }
        catch (NumberFormatException e) {
            logger.error("недопустимое значение {}", parameterName);
        }

        return result;
    }

}
