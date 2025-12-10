package com.campusmatrix.repository;

import com.campusmatrix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByStudentId(String studentId);
    Boolean existsByEmail(String email);
    Boolean existsByStudentId(String studentId);
}