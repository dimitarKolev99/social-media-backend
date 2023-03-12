package com.company.socialmedia.backend.api.post.dto;

import com.company.socialmedia.backend.api.comments.Comment;
import com.company.socialmedia.backend.api.files.FileInfo;
import com.company.socialmedia.backend.api.user.dto.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "post_description")
    @Expose
    private String description;

    @Column(name = "post_likes")
    @Expose
    private Integer likeCount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    @OneToOne()
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    public Post() {

    }

    public Post(String description) {
        this.description = description;
    }

    /**
     * Gets the UserContext value as JSON string.
     *
     * @return the content as a JSON object.
     */
    @JsonIgnore
    public String convertToJsonString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonString = gson.toJson(this);

        return jsonString;
    }

    /**
     * Allows to create a basic UserContext back from the String representation.
     *
     * @param jsonString The JSON string.
     *
     * @return A UserContext object.
     */
    @JsonIgnore
    public static Post fromJsonString(String jsonString) {
        String decodedString = new String(Base64.getDecoder().decode(jsonString));
        Gson gson = new GsonBuilder().create();
        Post context = gson.fromJson(decodedString, Post.class);

        return context;
    }
}
