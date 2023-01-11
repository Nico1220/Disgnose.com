package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByMail(String email);
}
