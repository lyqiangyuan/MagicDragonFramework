package com.hqy.mdf.web.starter.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author hqy
 * @date 2025/5/16
 */
@Slf4j
@Order(value = 20000)
@Component
public class WebLogFilter implements Filter {

    private static final List<String> BINARY_CONTENT_TYPES = Arrays.asList(
            "application/octet-stream",
            "image/",
            "video/",
            "audio/",
            "application/zip",
            "application/pdf",
            "application/msword"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();

        // 包装原始请求以允许多次读取请求体
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // 打印请求信息
        logRequest(requestWrapper);

        // 继续处理请求
        chain.doFilter(requestWrapper, responseWrapper);

        // 打印响应信息
        logResponse(responseWrapper, startTime);

        // 将响应内容写回原始响应
        responseWrapper.copyBodyToResponse();
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("\n=============== Web Request Start ===============\n");
        requestLog.append("Method: ").append(request.getMethod()).append("\n");
        requestLog.append("URI: ").append(request.getRequestURI()).append("\n");
        requestLog.append("Headers: ").append(getHeadersInfo(request)).append("\n");
        requestLog.append("Query String: ").append(request.getQueryString()).append("\n");
        requestLog.append("Request Parameters: ").append(getParameters(request)).append("\n");
        requestLog.append("Request Body: ").append(getRequestPayload(request)).append("\n");
        requestLog.append("=============== Web Request End ===============");
        log.info(requestLog.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response, long startTime) {
        StringBuilder responseLog = new StringBuilder();
        responseLog.append("\n=============== Web Response Start ===============\n");
        responseLog.append("Status: ").append(response.getStatus()).append("\n");
        responseLog.append("Headers: ").append(getResponseHeadersInfo(response)).append("\n");
        responseLog.append("Content-Type: ").append(response.getContentType()).append("\n");
        responseLog.append("Content-Length: ").append(response.getContentSize()).append(" bytes\n");
         //检查是否是二进制响应类型
        if (!isBinaryContent(response.getContentType())) {
            // 打印响应体
            responseLog.append("Response Body: ").append(getResponsePayload(response)).append("\n");
        }
        responseLog.append("Time Taken: ").append(System.currentTimeMillis() - startTime).append("ms\n");
        responseLog.append("=============== Web Response End ===============");
        log.info(responseLog.toString());
    }

    private String getRequestPayload(ContentCachingRequestWrapper request) {
        if (request != null) {
            byte[] buf = request.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, request.getCharacterEncoding());
                } catch (Exception e) {
                    return "[Request payload could not be parsed]";
                }

            }
        }
        return "";
    }

    private String getResponsePayload(ContentCachingResponseWrapper response) {
        if (response != null) {
            byte[] buf = response.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf,StandardCharsets.UTF_8);
                } catch (Exception e) {
                    return "[Response payload could not be parsed]";
                }
            }
        }
        return "";
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    private Map<String, String> getResponseHeadersInfo(ContentCachingResponseWrapper response) {
        Map<String, String> map = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }
        return map;
    }

    private boolean isBinaryContent(String contentType) {
        if (contentType == null) {
            return false;
        }
        return BINARY_CONTENT_TYPES.stream().anyMatch(contentType::startsWith);
    }

    private String getParameters(ContentCachingRequestWrapper request) {
        // 这里只能获取参数名，不能获取文件内容
        // 完整的文件内容获取需要解析multipart请求，这超出了简单过滤器的范围
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder params = new StringBuilder();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            params.append(entry.getKey()).append("=");
            params.append(Arrays.toString(entry.getValue())).append(", ");
        }

        if (params.length() > 0) {
            params.setLength(params.length() - 2); // 移除最后的逗号和空格
        }

        return params.toString();
    }
}
