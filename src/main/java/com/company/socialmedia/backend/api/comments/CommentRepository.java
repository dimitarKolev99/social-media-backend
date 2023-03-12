package com.company.socialmedia.backend.api.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c INNER JOIN Post p WHERE c.id = ?1")
    List<Comment> findAllByPostId(Long postId);

}
