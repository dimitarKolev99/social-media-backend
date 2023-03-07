package com.company.socialmedia.backend.api.user.dto;


import com.company.socialmedia.backend.login.refreshtoken.RefreshToken;
import com.company.socialmedia.backend.login.role.Role;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Long id;

    @Column(name = "username")
    @NotBlank
    @Size(max = 20)
    @Expose
    private String username;

    @Column(name = "email")
    @NotBlank
    @Size(max = 50)
    @Email
    @Expose
    private String email;

    @Column(name = "password")
    @NotBlank
    @Size(max = 120)
    private String password;

    @Column
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    private RefreshToken refreshToken;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the UserContext value as JSON string.
     *
     * @return the content as a JSON object.
     */
    public String getAsJsonString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonString = gson.toJson(this);

        return jsonString;
    }

    /**
     * Allows to create a basic UserContext back from the String representation.
     *
     * @param jsonString The JSON string.
     *
     * @return A UserContext object.
     */
    public static User fromJsonString(String jsonString) {
        String decodedString = new String(Base64.getDecoder().decode(jsonString));
        Gson gson = new GsonBuilder().create();
        User context = gson.fromJson(decodedString, User.class);

        return context;
    }
}
