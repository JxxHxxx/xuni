package com.xuni.api.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("/check-health")
    public String healthCheck() {
        return "ok";
    }
}