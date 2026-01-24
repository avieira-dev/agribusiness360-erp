package com.agribusiness360.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agribusiness360.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Search for a user by username
    Optional<User> findByUsername(String username);

    // Search for a user by email
    Optional<User> findByEmail(String email);

    // Search for a user by unique code
    Optional<User> findByCode(String code);
}
