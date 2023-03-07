package com.company.socialmedia.backend.api.files;

import com.company.socialmedia.backend.api.post.PostRepository;
import com.company.socialmedia.backend.api.post.dto.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
@CrossOrigin("http://localhost:8081")
public class FilesController {

    @Autowired
    FilesStorageService storageService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FileInfoRepository fileInfoRepository;

    @PostMapping("/posts/{postId}/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @PathVariable(value = "postId") Long postId) {
        String message = "";
        try {
            Path filePath = storageService.save(file);

            Optional<Post> post = postRepository.findById(postId);

            if (post.isPresent()) {
                Post post1 = post.get();
                FileInfo fileInfo = new FileInfo();
                fileInfo.setUri(filePath.toUri().toString());

                post1.setFileInfo(fileInfo);

                fileInfoRepository.save(fileInfo);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } else {
                message = "Error uploading file: Post not found";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String fileName = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(url);
        }
        ).collect(Collectors.toList());


        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
