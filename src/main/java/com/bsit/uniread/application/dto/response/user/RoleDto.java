package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleDto {
    private Integer id;
    private RoleName roleName;

    public RoleDto(Role role){
        this.id = role.getId();
        this.roleName = role.getName();
    }
}
