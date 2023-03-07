package com.company.socialmedia.backend.api.user;

import com.company.socialmedia.backend.exception.ResourceNotFoundException;
import com.company.socialmedia.backend.api.user.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    public ResponseEntity<List<User>> getAllUsers(String username) {
        List<User> userList = new ArrayList<>();

        if (username == null) {
            userList.addAll(userRepository.findAll());
        } else {
            userList.add(userRepository.findByUsername(username));
        }

        if (userList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    public ResponseEntity<User> getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + id));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<User>  createUser(User user) {
        LOGGER.info("Creating a user");

        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<User> updateUser(Long id, User user) {
        User _user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id)
                );

        _user.setUsername(user.getUsername());
        _user.setPassword(user.getPassword());
        _user.setEmail(user.getEmail());

        return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteUser(Long id) {
        userRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAllUsers() {
        userRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
