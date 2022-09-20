package com.ekomora.springjwt.repository;

import java.util.Optional;

import com.ekomora.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstName(String username);

    Optional<User> findByEmail(String email);

//    Boolean existsByFirstName(String username);

    Boolean existsByEmail(String email);
}
