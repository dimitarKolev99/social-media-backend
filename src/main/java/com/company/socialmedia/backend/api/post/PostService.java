package com.company.socialmedia.backend.api.post;

import com.company.socialmedia.backend.api.post.dto.Post;
import com.company.socialmedia.backend.api.post.dto.request.PostRequest;
import com.company.socialmedia.backend.api.user.UserRepository;
import com.company.socialmedia.backend.api.user.dto.User;
import com.company.socialmedia.backend.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Map<String, Object>> getAllPosts(int page, int size) {

        List<Post> postList;
        Pageable paging = PageRequest.of(page, size);

        Page<Post> postPage = postRepository.findAll(paging);

        postList = postPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("posts", postList);
        response.put("currentPage", postPage.getNumber());
        response.put("totalItems", postPage.getTotalElements());
        response.put("totalPages", postPage.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> addPost(PostRequest postRequest) {

        UserContext userContext = UserContext.getCurrentUser();

        User user = userRepository.findByUsername(userContext.getUsername());

        Post post = new Post(postRequest.getDescription());
        postRepository.save(post);

        return ResponseEntity.ok("Post created successfully");
    }
}
