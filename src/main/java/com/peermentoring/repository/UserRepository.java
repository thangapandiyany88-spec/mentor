package com.peermentoring.repository;

import com.peermentoring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository interface extending JpaRepository.
 * 
 * Educational Explanation for Viva:
 * JpaRepository provides CRUD operations (save, findById, findAll, deleteById) automatically.
 * Spring Data JPA auto-generates SQL queries based on method names like findByEmail.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
