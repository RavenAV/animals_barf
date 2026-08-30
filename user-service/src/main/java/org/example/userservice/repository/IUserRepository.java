package org.example.userservice.repository;

import org.example.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndEnabledTrue(String email);
    Optional<User> findByIdAndEnabledTrue(Long id);
    //String findNameById(Long Id);
    boolean existsByEmail(String email);
}
