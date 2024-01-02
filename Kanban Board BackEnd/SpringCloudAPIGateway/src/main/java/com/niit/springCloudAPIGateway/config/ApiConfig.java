package com.niit.springCloudAPIGateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/v1/**")
                        .uri("lb://user-authentication-service/"))

                .route(p->p
                        .path("/api/v2/**","/api/v2/register")
                        .uri("lb://user-kanban-service/"))

                .route(p -> p
                        .path("/api/v3/**")
                        .uri("lb://user-notification-service/"))
                .route(p -> p
                        .path("/api/v4/**")
                        .uri("lb://user-email-service/"))
                .build();
    }
}
