package com.company.socialmedia.backend.api.post.dto.response;

import lombok.Data;

public @Data class PostWithUser {

    private Long id;

    private String description;

    private String username;

    public PostWithUser() {

    }

    public PostWithUser(Long id, String description, String username) {
        this.id = id;
        this.description = description;
        this.username = username;
    }
}
