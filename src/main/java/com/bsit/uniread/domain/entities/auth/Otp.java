package com.bsit.uniread.domain.entities.auth;

import com.bsit.uniread.domain.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "otps", indexes = {
        @Index(name = "idx_otps_email", columnList = "email"),
        @Index(name = "idx_otps_code", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;
    private String code;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
