package com.company.socialmedia.backend.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSecurityFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {

    }

    protected HttpServletResponse applyHeaders(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Allow-Headers",
                "x-requested-with, authorization, Content-Type, " +
                        "Authorization");

        return httpResponse;
    }

    @Override
    public void destroy() {

    }

}
