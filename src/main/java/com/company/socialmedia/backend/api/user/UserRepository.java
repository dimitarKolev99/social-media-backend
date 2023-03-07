package com.company.socialmedia.backend.api.user;

import com.company.socialmedia.backend.api.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(@Param("username") String username);

    List<User> findByIdIn(List<Long> ids);

    @Query( "select username from User u where id in :ids" )
    List<String> findUsernameByIdIn(List<Long> ids);

//    @Query("select username from User u inner join Post p where :id = p.id")
//    List<String> findUsernameById(Long id);

    List<String> findUsernameById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
