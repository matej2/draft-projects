package com.doctor.file_processor.middleware;

import com.doctor.file_processor.domain.entity.AccessInfo;
import com.doctor.file_processor.service.AccessInfoService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter
public class RateLimitFilter implements Filter {

    private final AccessInfoService accessInfoService;

    public RateLimitFilter(AccessInfoService accessInfoService) {
        this.accessInfoService = accessInfoService;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // Initialization logic, if any
    }

    @Override
    public synchronized void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String remoteAddr = request.getRemoteAddr();

        AccessInfo existingAccInfo = accessInfoService.findByIp(remoteAddr);
        Integer CALL_LIMIT = 5;

        // Ideally create a private token that can be used only with single IP
        if (existingAccInfo == null) {
            existingAccInfo = accessInfoService.createEntry(remoteAddr);
        }

        if (existingAccInfo.getNumOfCallsLastMinute() <= CALL_LIMIT) {
            accessInfoService.updateAccessInfo(remoteAddr);
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendError(429, "Maximum number of calls reached");
        }
    }

    @Override
    public void destroy() {
        // Cleanup logic, if any
    }
}