package com.uniread.admin.controllers;

import com.uniread.admin.dto.request.UserMonitoringFilter;
import com.uniread.admin.dto.response.UserMonitoringDto;
import com.uniread.admin.services.AdminUserService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService userService;

    @GetMapping
    public ResponseEntity<Page<UserMonitoringDto>> getUsersMonitoring(
        @ModelAttribute UserMonitoringFilter filter,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");
        var payload = userService.getUsersMonitor(filter, userDetails);
        return ResponseEntity.ok(payload);
    }
}
