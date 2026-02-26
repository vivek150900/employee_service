package com.employee_service.config;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public Optional<String> getCurrentAuditor() {
        String username = request.getHeader("X-User-Name");
        return Optional.ofNullable(username != null ? username : "SYSTEM");
    }
}
