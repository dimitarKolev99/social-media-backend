package com.company.socialmedia.backend.login.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class LoginRequest {
    private String username;
    private String password;
}
