package com.oleh.chui.learning_platform.service;

import com.oleh.chui.learning_platform.entity.Role;
import com.oleh.chui.learning_platform.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleAndCreateIfNeed(Role.RoleEnum roleEnum) {
        Optional<Role> role = roleRepository.findByRole(roleEnum);

        return role.orElseGet(() -> roleRepository.save(Role.builder().role(roleEnum).build()));
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
