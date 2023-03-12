package com.company.socialmedia.backend.api.post.dto.request;

import lombok.Data;

public @Data class UpdatePostLikesRequest {

    private Integer numberLikes;

    public UpdatePostLikesRequest() {

    }

    public UpdatePostLikesRequest(Integer numberLikes) {
        this.numberLikes = numberLikes;
    }

}
