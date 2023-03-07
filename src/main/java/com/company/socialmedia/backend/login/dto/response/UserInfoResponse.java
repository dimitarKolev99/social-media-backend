package com.company.socialmedia.backend.login.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class UserInfoResponse {
    private long id;
    private String username;
    private String email;
    private List<String> role;

    public UserInfoResponse(long id, String username, String email, List<String> role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
