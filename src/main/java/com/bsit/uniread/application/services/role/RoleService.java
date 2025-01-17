package com.bsit.uniread.application.services.role;

import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.RoleName;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to get resource"));
    }

    public Role getAdminRole() {
        return roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to get resource"));
    }

    public Role getSuperAdminRole() {
        return roleRepository.findByName(RoleName.SUPER_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to get resource"));
    }
}
