package com.company.socialmedia.backend.api.files;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "file_info")
public @Data class FileInfo {

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_uri")
    private String uri;

    public FileInfo() {

    }

    public FileInfo(String uri) {
        this.uri = uri;
    }

}
