package com.oleh.chui.learning_platform.repository;

import com.oleh.chui.learning_platform.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(Role.RoleEnum role);

}
