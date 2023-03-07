package com.company.socialmedia.backend.api.post.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public @Data class AllPostsResponse {
    private List<PostWithUser> posts = new ArrayList<>();
    public AllPostsResponse() {

    }

    public AllPostsResponse(List<PostWithUser> posts) {
        this.posts = posts;
    }
}
