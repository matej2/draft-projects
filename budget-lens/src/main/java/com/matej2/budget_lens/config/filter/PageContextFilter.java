package com.matej2.budget_lens.config.filter;

import com.matej2.budget_lens.controller.WebModelProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PageContextFilter extends OncePerRequestFilter {
    private final WebModelProvider webModelProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.setAttribute("pageUrlMapping", webModelProvider.getPageContext());
        filterChain.doFilter(request, response);
    }
}
