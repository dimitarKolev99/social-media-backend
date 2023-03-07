package com.company.socialmedia.backend.api.post;

import com.company.socialmedia.backend.api.post.dto.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @jakarta.transaction.Transactional
    void deleteById(Long id);

    List<Post> findByUserId(Long userId);

    @Transactional
    void deleteByUserId(Long userId);

}
