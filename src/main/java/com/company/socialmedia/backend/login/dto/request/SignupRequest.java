package com.company.socialmedia.backend.login.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Set<String> role;
}
