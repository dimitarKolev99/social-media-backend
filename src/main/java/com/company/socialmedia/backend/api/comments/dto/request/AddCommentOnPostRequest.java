package com.company.socialmedia.backend.api.comments.dto.request;

import lombok.Data;

public @Data class AddCommentOnPostRequest {

    private String message;

    public AddCommentOnPostRequest() {

    }

    public AddCommentOnPostRequest(String message) {
        this.message = message;
    }

}
