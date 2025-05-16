package com.hqy.mdf.web.starter.filter;

import com.hqy.mdf.common.constant.BaseConst;
import com.hqy.mdf.common.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author hqy
 */
@Slf4j
@Order
@WebFilter(filterName="webTraceIdFilter",urlPatterns="/*")
public class WebTraceIdFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String traceId = null;
        if (request instanceof HttpServletRequest) {
            //获取请求头traceId
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            traceId = httpServletRequest.getHeader(BaseConst.TRACE_ID);

            if (StringUtils.isEmpty(traceId)) {
                //如果请求头中没有traceId，从cookie中获取traceId
                Cookie[] cookies = httpServletRequest.getCookies();
                if (cookies != null) {
                    for (int i = 0; i < cookies.length; i++) {
                        if (BaseConst.TRACE_ID.equals(cookies[i].getName()) && !StringUtils.isEmpty(cookies[i].getValue())) {
                            traceId = cookies[i].getValue();
                            break;
                        }
                    }
                }
            }
        }

        if (StringUtils.isEmpty(traceId)) {
            //如果请求头和cookies中都没有traceId，从请求参数中获取traceId
            traceId = request.getParameter(BaseConst.TRACE_ID);
        }
        LogUtils.setTraceId(traceId);
        chain.doFilter(request, response);
        LogUtils.removeTraceId();

    }
}
