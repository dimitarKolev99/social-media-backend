package com.company.socialmedia.backend.api.comments;

import com.company.socialmedia.backend.api.comments.dto.request.AddCommentOnPostRequest;
import com.company.socialmedia.backend.api.files.FileInfoRepository;
import com.company.socialmedia.backend.api.files.FilesStorageService;
import com.company.socialmedia.backend.api.post.PostRepository;
import com.company.socialmedia.backend.api.post.PostService;
import com.company.socialmedia.backend.api.post.dto.Post;
import com.company.socialmedia.backend.api.user.UserRepository;
import com.company.socialmedia.backend.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:9000", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CommentController {

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

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/posts/{id}/comments")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Comment>> getPostCommentsById(@PathVariable(value = "id") Long id) {
        List<Comment> list = commentRepository.findAllByPostId(id);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/posts/{id}/comments")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Comment>> addCommentOnPost(@PathVariable(value = "id") Long id, @RequestBody AddCommentOnPostRequest addCommentOnPostRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Post with id = " + id));

        Comment newComment = new Comment();
        newComment.setMessage(addCommentOnPostRequest.getMessage());

        commentRepository.save(newComment);

        post.getComments().add(newComment);

        return new ResponseEntity<>(post.getComments(), HttpStatus.OK);
    }



}
