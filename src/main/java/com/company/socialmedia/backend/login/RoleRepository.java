package com.company.socialmedia.backend.login;

import com.company.socialmedia.backend.login.role.ERole;
import com.company.socialmedia.backend.login.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(ERole name);

}
