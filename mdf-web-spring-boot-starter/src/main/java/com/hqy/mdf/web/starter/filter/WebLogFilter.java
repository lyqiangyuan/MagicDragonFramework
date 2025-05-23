package com.hqy.mdf.web.starter.filter;

import com.hqy.mdf.web.starter.wrapper.BodyCachingRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author hqy
 */
@Slf4j
@Order(value = 20000)
@WebFilter(filterName = "webLogFilter")
public class WebLogFilter extends OncePerRequestFilter {

    final int maxPayloadLength = 1024 * 10;

    final List<String> printableContentTypes = Arrays.asList(
            "application/json",
            "text/plain",
            "text/json",
            "application/xml"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        // 包装原始请求以允许多次读取请求体
        BodyCachingRequestWrapper requestWrapper = new BodyCachingRequestWrapper(request);
//        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        //记录请求
        logRequest(requestWrapper);
        //继续处理请求
        filterChain.doFilter(requestWrapper, responseWrapper);
        //记录响应
        logResponse(responseWrapper, startTime);
        // 将响应内容写回原始响应
        responseWrapper.copyBodyToResponse();

    }

    private boolean shouldLogBody(String contentType) {
        return contentType != null &&
                printableContentTypes.stream().anyMatch(contentType::startsWith);
    }


    private void logRequest(HttpServletRequest request) throws IOException {
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("\n========== Log Request Start ==========\n");
        requestLog.append("URI: ").append(request.getRequestURI()).append("\n");
        requestLog.append("Method: ").append(request.getMethod()).append("\n");
        requestLog.append("RemoteAddr: ").append(request.getRemoteAddr()).append("\n");
        requestLog.append("Headers: ").append(getHeadersInfo(request)).append("\n");
        requestLog.append("Parameters: ").append(getParameters(request)).append("\n");
        if (shouldLogBody(request.getContentType())) {
            requestLog.append("Body: ").append(getRequestPayload(request)).append("\n");
        }
        requestLog.append("========== Log Request End ==========");
        log.info(requestLog.toString());
    }

    private void logResponse(HttpServletResponse response, long startTime) {
        StringBuilder responseLog = new StringBuilder();
        responseLog.append("\n========== Log Response Start ==========\n");
        responseLog.append("Status: ").append(response.getStatus()).append("\n");
        responseLog.append("Headers: ").append(getResponseHeadersInfo(response)).append("\n");
        responseLog.append("Content-Type: ").append(response.getContentType()).append("\n");
        if (shouldLogBody(response.getContentType())) {
            // 打印响应体
            responseLog.append("Body: ").append(getResponsePayload(response)).append("\n");
        }
        responseLog.append("Time Taken: ").append(System.currentTimeMillis() - startTime).append("ms\n");
        responseLog.append("========== Log Response End ==========");
        log.info(responseLog.toString());
    }

    private String getRequestPayload(HttpServletRequest request) {
        if (request instanceof BodyCachingRequestWrapper) {
            if (request.getContentLength() > maxPayloadLength) {
                return "[Request payload is too large to log]";
            }
            try {
                byte[] buf = ((BodyCachingRequestWrapper) request).getRequestBody();
                if (buf.length > 0) {
                    return new String(buf, request.getCharacterEncoding());
                }
            } catch (Exception e) {
                return "[Request payload could not be parsed]";
            }

        }
        return null;
    }

    private String getResponsePayload(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            if (responseWrapper.getContentSize() > maxPayloadLength) {
                return "[Response payload is too large to log]";

            }
            byte[] buf = responseWrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    return "[Response payload could not be parsed]";
                }
            }
        }
        return null;
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

    private Map<String, String> getResponseHeadersInfo(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }
        return map;
    }

    private String getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder params = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (!first) {
                params.append(", ");
            }
            params.append(entry.getKey()).append("=");
            params.append(Arrays.toString(entry.getValue()));
            first = false;
        }
        return params.toString();
    }
}
