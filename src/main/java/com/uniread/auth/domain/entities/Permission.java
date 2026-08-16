package com.uniread.auth.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions", indexes = {
    @Index(name = "idx_permissions_code", columnList = "code"),
    @Index(name = "idx_permissions_resource", columnList = "resource")
})
@Entity
@Builder
@Data
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    private String resource;
    private String displayName;
    private String action;
    private String description;

}
