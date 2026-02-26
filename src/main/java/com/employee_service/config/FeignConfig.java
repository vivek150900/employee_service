package com.employee_service.config;


import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return template -> {

            RequestAttributes requestAttributes =
                    RequestContextHolder.getRequestAttributes();

            if (requestAttributes instanceof ServletRequestAttributes attributes) {

                HttpServletRequest request = attributes.getRequest();

                String username = request.getHeader("X-User-Name");
                String role = request.getHeader("X-User-Role");
                String auth = request.getHeader("Authorization");

                if (username != null)
                    template.header("X-User-Name", username);

                if (role != null)
                    template.header("X-User-Role", role);

                if (auth != null)
                    template.header("Authorization", auth);
            }
        };
    }
}