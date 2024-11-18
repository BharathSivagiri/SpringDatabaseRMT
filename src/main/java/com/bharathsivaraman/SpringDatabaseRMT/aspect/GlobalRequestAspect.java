package com.bharathsivaraman.SpringDatabaseRMT.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

@Aspect
@Component
@Order(-1)
public class GlobalRequestAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* com.bharathsivaraman.SpringDatabaseRMT.controller.*.*(..))")
    public Object interceptRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String encryptedData = request.getHeader("x-encrypt");
        if (encryptedData == null || encryptedData.isEmpty()) {
            throw new IllegalArgumentException("x-encrypt header is required");
        }

        try {
            // Get method parameters
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Class<?>[] parameterTypes = signature.getParameterTypes();

            // Find the model class (usually the last parameter)
            Class<?> targetClass = parameterTypes.length > 0 ? parameterTypes[parameterTypes.length - 1] : null;

            if (targetClass == null) {
                throw new IllegalArgumentException("No model class found in method parameters");
            }

            JsonNode rootNode = objectMapper.readTree(encryptedData);

            // Extract headers
            Map<String, String> headers = new HashMap<>();
            this.setHeader(rootNode, headers);

            // Extract param
            Map<String, String> param = new HashMap<>();
            this.setParam(rootNode, param);

            // Get request body and convert to target class
            JsonNode requestBodyNode = rootNode.path("requestBody");
            Object requestBody = null;
            if (!requestBodyNode.isEmpty()){
                requestBody = objectMapper.treeToValue(requestBodyNode, targetClass);
            }
            // Create arguments array
            Object[] arguments = new Object[parameterTypes.length];
            int headerIndex = 0;
            int paramIndex = 0;
            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i].equals(targetClass) && requestBody != null) {
                    arguments[i] = requestBody;
                } else if (headerIndex < headers.size()) {
                    arguments[i] = headers.values().toArray()[headerIndex++];
                } else if (!param.isEmpty()) {
                    arguments[i] = param.values().toArray()[paramIndex++];
                }
            }

            return joinPoint.proceed(arguments);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid data in x-encrypt header: " + e.getMessage());
        }
    }

    private void setHeader(JsonNode rootNode, Map<String, String> headers) {
        JsonNode headersNode = rootNode.path("headers");
        Iterator<Map.Entry<String, JsonNode>> headerFields = headersNode.fields();
        while (headerFields.hasNext()) {
            Map.Entry<String, JsonNode> field = headerFields.next();
            if (!field.getValue().asText().isEmpty()) {
                headers.put(field.getKey(), field.getValue().asText());
            }
        }
    }

    public void setParam(JsonNode rootNode , Map<String, String> param) {
        JsonNode paramNode = rootNode.path("requestParam");
        Iterator<Map.Entry<String, JsonNode>> paramFields = paramNode.fields();
        while (paramFields.hasNext()) {
            Map.Entry<String, JsonNode> field = paramFields.next();
            if (!field.getValue().asText().isEmpty()) {
                param.put(field.getKey(), field.getValue().asText());
            }
        }
    }
}