package com.doctor.file_processor.middleware;

import com.doctor.file_processor.domain.entity.AccessInfo;
import com.doctor.file_processor.service.AccessInfoService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@WebFilter
public class RateLimitFilter implements Filter {

    private final AccessInfoService accessInfoService;
    private final Integer rateLimit;

    public RateLimitFilter(AccessInfoService accessInfoService, Integer rateLimit) {
        this.accessInfoService = accessInfoService;
        this.rateLimit = rateLimit;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // Initialization logic, if any
    }

    @Override
    public synchronized void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String remoteAddr = request.getRemoteAddr();

        AccessInfo existingAccInfo = accessInfoService.findByIp(remoteAddr);

        // Ideally create a private token that can be used only with single IP
        if (existingAccInfo == null) {
            existingAccInfo = accessInfoService.createEntry(remoteAddr);
        }

        if (existingAccInfo.getNumOfCallsLastMinute() <= rateLimit) {
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