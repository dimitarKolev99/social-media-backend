package com.company.socialmedia.backend.api.post;

import com.company.socialmedia.backend.exception.ResourceNotFoundException;
import com.company.socialmedia.backend.api.post.dto.Post;
import com.company.socialmedia.backend.api.post.dto.request.PostRequest;
import com.company.socialmedia.backend.api.user.UserRepository;
import com.company.socialmedia.backend.api.user.dto.User;
import com.company.socialmedia.backend.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class PostController {

    public static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private  static  final Random RANDOM = new Random();

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping("/posts")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "3") int size) {
        LOGGER.debug("Get all posts");

        return postService.getAllPosts(page, size);
    }

    @GetMapping("/users/{id}/posts")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Post>> getAllPostsByUserId(@PathVariable(value = "id") Long userId) {
        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }


        List<Post> postList = postRepository.findByUserId(userId);

        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Post> getPostById(@PathVariable(value = "id") Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Post with id = " + id));

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/posts")
    public ResponseEntity<Post> createPost(@PathVariable(value = "userId") Long userId,
                                                 @RequestBody PostRequest postRequest) {
        Post postReturn = userRepository.findById(userId).map(user -> {
            Post post = new Post();
            post.setDescription(postRequest.getDescription());
            post.setUser(user);
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + userId));

        return new ResponseEntity<>(postReturn, HttpStatus.CREATED);
    }

    @PostMapping("/posts")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Post> addPostOnCurrentUser(@RequestBody PostRequest postRequest) {

        UserContext userContext = UserContext.getCurrentUser();

        Long userId = userContext.getId();

        Post postReturn = userRepository.findById(userId).map(user -> {
            Post post = new Post();
            post.setDescription(postRequest.getDescription());
            post.setUser(user);
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + userId));

        return new ResponseEntity<>(postReturn, HttpStatus.CREATED);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody PostRequest postRequest) {
        Post postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommentId " + id + "not found"));

        postEntity.setDescription(postRequest.getDescription());

        return new ResponseEntity<>(postRepository.save(postEntity), HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable("id") Long id) {
        postRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/users/{userId}/posts")
    public ResponseEntity<HttpStatus> deleteAllPostsOfUser(@PathVariable(value = "userId") Long userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));

        Optional<Post> postToDelete = postRepository.findById(user.getId());

        if (postToDelete.isPresent()) {
            postRepository.deleteById(postToDelete.get().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
