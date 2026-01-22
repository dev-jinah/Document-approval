package com.docapproval.api.common;

import com.docapproval.api.service.ApiKeyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final String API_KEY_HEADER = "X-API-Key";

    private final ApiKeyService apiKeyService;
    private final ObjectMapper objectMapper;

    public ApiKeyInterceptor(ApiKeyService apiKeyService, ObjectMapper objectMapper) {
        this.apiKeyService = apiKeyService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // OPTIONS 요청은 CORS preflight이므로 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String apiKey = request.getHeader(API_KEY_HEADER);

        if (!apiKeyService.validateApiKey(apiKey)) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, apiKeyService.getErrorMessage());
            return false;
        }

        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws Exception {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(errorBody));
    }
}