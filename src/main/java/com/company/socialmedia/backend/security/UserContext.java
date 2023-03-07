package com.company.socialmedia.backend.security;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class UserContext implements AutoCloseable{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserContext.class);

    /**
     * Stores the user on the thread.
     */
    private static ThreadLocal<UserContext> instance = new ThreadLocal<>();

    private Long id;
    private String username;

    private UserContext(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * Getter for the current user.
     *
     * @return the current user.
     */
    public static UserContext getCurrentUser() {
        UserContext userContext = instance.get();

        return userContext;
    }

    /**
     * Call this to create an instance of UserContext and store it in the thread
     * context.
     *
     * @return A UserContext object.
     */
    public static UserContext create(Long id, String username) {
        UserContext userContext = new UserContext(id, username);

        instance.set(userContext);

        return userContext;
    }

    /**
     * Closes. Removes the ThreadContext value from the thread.
     */
    @Override
    public void close() throws Exception {
        instance.remove();
    }
}
