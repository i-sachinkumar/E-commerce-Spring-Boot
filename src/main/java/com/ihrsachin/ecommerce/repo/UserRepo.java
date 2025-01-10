package com.ihrsachin.ecommerce.repo;

import com.ihrsachin.ecommerce.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(@NotBlank @Size(min = 3, max = 20) String username);

    boolean existsByEmail(@NotBlank @Email String email);
}
