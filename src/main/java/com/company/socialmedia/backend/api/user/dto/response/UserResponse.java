package com.company.socialmedia.backend.api.user.dto.response;

import com.company.socialmedia.backend.api.user.dto.User;
import lombok.Data;

import java.util.List;

public @Data class UserResponse {
    private List<User> allUsers;
}
