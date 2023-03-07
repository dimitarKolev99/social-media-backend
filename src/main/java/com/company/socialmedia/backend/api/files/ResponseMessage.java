package com.company.socialmedia.backend.api.files;

import lombok.Data;

public @Data class ResponseMessage {
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }
}
