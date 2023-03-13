package com.company.socialmedia.backend.api.post.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class PostRequest {

    private MultipartFile file;

    private String description;



}
