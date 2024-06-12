package ru.belonogov.task_service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.belonogov.task_service.domain.dto.request.CompanyUpdateRequest;
import ru.belonogov.task_service.domain.dto.response.CompanyResponse;
import ru.belonogov.task_service.domain.exception.UnsupportedMediaTypeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ConverterTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    Converter converter;

    @Test
    void getRequestBody_shouldReturnCompanyDTO_whenJson() throws IOException {
        CompanyUpdateRequest companyUpdateRequest = new CompanyUpdateRequest();
        BufferedReader bufferedReader = mock(BufferedReader.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContentType()).thenReturn("application/json"); //application/pdf
        when(request.getReader()).thenReturn(bufferedReader);
        when(objectMapper.readValue(bufferedReader, CompanyUpdateRequest.class)).thenReturn(companyUpdateRequest);

        CompanyUpdateRequest result = converter.getRequestBody(request, CompanyUpdateRequest.class);

        assertThat(result).isEqualTo(companyUpdateRequest);
    }

    @Test
    void getRequestBody_shouldReturnUnsupportedMediaTypeException_whenOtherContentType() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContentType()).thenReturn("application/pdf");

        assertThrows(UnsupportedMediaTypeException.class, () -> converter.getRequestBody(request, CompanyUpdateRequest.class));
    }

    @Test
    void getResponseBody_whenOtherContentType() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        CompanyResponse companyResponse = new CompanyResponse();
        PrintWriter printWriter= mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        converter.getResponseBody(response, companyResponse);

        verify(objectMapper).writeValue(printWriter, companyResponse);
    }

    @Test
    void getString() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String param = "name";
        String response = "response";
        when(request.getParameter(param)).thenReturn(response);

        String result = converter.getString(request, param);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void getLong() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        String param = "name";
        Long response = 5L;
        when(req.getParameter("name")).thenReturn("5");

        Long result = converter.getLong(req, param);

        assertThat(result == response).isTrue();
    }
}