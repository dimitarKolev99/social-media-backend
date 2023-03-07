package com.company.socialmedia.backend.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSecurityFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
