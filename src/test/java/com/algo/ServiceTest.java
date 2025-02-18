package com.algo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class ServiceTest {
    @PreAuthorize("hasRole('ROLE_USER')")
    public String ok() {
        return "ok";
    }
}
