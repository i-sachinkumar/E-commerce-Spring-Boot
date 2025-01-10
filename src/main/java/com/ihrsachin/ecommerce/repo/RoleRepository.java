package com.ihrsachin.ecommerce.repo;

import com.ihrsachin.ecommerce.model.AppRole;
import com.ihrsachin.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRoleName(AppRole appRole);
}