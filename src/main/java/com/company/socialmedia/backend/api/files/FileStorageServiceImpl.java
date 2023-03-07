package com.company.socialmedia.backend.api.files;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FilesStorageService{

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Path save(MultipartFile file) {
        try {

            LOGGER.debug("Original File Name: {}", file.getOriginalFilename());

            String randomLongInString = String.valueOf(new Random().nextLong());

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

            assert fileExtension != null;
            String newFileName = randomLongInString.concat("." + fileExtension);

            LOGGER.debug("NewFileName: {}", newFileName);

            Path newFilePath = Paths.get(newFileName);

            LOGGER.debug("NewFilePath: {}", newFilePath);

            LOGGER.debug("File Extension: {}", FilenameUtils.getExtension(String.valueOf(file)));

            Files.copy(file.getInputStream(), this.root.resolve(newFilePath));

            return this.root.resolve(newFilePath);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files");
        }
    }
}
