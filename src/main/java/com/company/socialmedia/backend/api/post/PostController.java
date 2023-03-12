package com.company.socialmedia.backend.api.post;

import com.company.socialmedia.backend.api.files.FileInfo;
import com.company.socialmedia.backend.api.files.FileInfoRepository;
import com.company.socialmedia.backend.api.files.FilesStorageService;
import com.company.socialmedia.backend.api.files.ResponseMessage;
import com.company.socialmedia.backend.api.post.dto.request.UpdatePostLikesRequest;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.*;

@CrossOrigin(origins = "http://localhost:9000", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class PostController {

    public static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    FileInfoRepository fileInfoRepository;

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

    @PostMapping(value = "/posts", consumes = { MediaType.APPLICATION_JSON_VALUE,
                MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Post> addPostOnCurrentUser(@RequestParam(value = "file", required = false) MultipartFile file,
                                                     @RequestPart("post") PostRequest postRequest) {

        UserContext userContext = UserContext.getCurrentUser();

        Long userId = userContext.getId();

        Post postReturn = userRepository.findById(userId).map(user -> {
            Post post = new Post();
            post.setDescription(postRequest.getDescription());
            post.setUser(user);
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Post with id = " + userId));

        String message;

        if (file != null) {

            try {
                Path filePath = storageService.save(file);

                FileInfo fileInfo = new FileInfo();
                fileInfo.setUri(filePath.toUri().toString());

                postReturn.setFileInfo(fileInfo);

                fileInfoRepository.save(fileInfo);

                message = "Post created: " + file.getOriginalFilename();

                return new ResponseEntity<>(postReturn, HttpStatus.CREATED);

            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
                return new ResponseEntity<>(postReturn, HttpStatus.EXPECTATION_FAILED);
            }
        }

        return new ResponseEntity<>(postReturn, HttpStatus.CREATED);
    }

    @GetMapping(value = "/user/picture")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getProfilePicture() {
        UserContext userContext = UserContext.getCurrentUser();

        Long userId = userContext.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));

        if (user.getFileInfo() != null && user.getFileInfo().getUri() != null) {
            return new ResponseEntity<>(user.getFileInfo().getUri(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResponseMessage("No user pic uploaded"), HttpStatus.EXPECTATION_FAILED);
    }


    @PostMapping(value = "/user/picture", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadProfilePic(@RequestParam(value = "file") MultipartFile file) {
        UserContext userContext = UserContext.getCurrentUser();

        Long userId = userContext.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));

        String message;

        Path filePath = null;

        try {
            filePath = storageService.save(file);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setUri(filePath.toUri().toString());

            user.setFileInfo(fileInfo);

            fileInfoRepository.save(fileInfo);

            message = "User pic uploaded: " + file.getOriginalFilename();

            return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage(filePath.toString()), HttpStatus.EXPECTATION_FAILED);
        }
    }


    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody PostRequest postRequest) {
        Post postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommentId " + id + "not found"));

        postEntity.setDescription(postRequest.getDescription());

        return new ResponseEntity<>(postRepository.save(postEntity), HttpStatus.OK);
    }

    @PutMapping("/posts/{id}/likes")
    public ResponseEntity<Post> updatePostLikes(@PathVariable("id") Long id, @RequestBody UpdatePostLikesRequest updatePostLikesRequest) {
        Post postEntity = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommentId " + id + "not found"));

        postEntity.setLikeCount(updatePostLikesRequest.getNumberLikes());

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
