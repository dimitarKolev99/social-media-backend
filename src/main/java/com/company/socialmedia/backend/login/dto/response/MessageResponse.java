package com.company.socialmedia.backend.login.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
