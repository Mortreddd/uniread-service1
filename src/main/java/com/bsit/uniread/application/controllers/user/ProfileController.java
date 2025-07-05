package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.profile.ProfileDashboardDto;
import com.bsit.uniread.application.services.profile.ProfileService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = ApiEndpoints.PROFILE)
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping(path = "/dashboard")
    public ResponseEntity<ProfileDashboardDto> getUserDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(profileService.getUserDashboard(userDetails));
    }

    @GetMapping(path = "/stories")
    public ResponseEntity<Page<BookDto>> getUserBooks(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "category", required = false, defaultValue = "ALL") String category,
            @RequestParam(name = "query", required = false, defaultValue = "") String query,
            @RequestParam(name = "sortBy", required = false, defaultValue = "asc") String sortBy,
            @RequestParam(name = "orderBy", required = false, defaultValue = "createdAt") String orderBy,
            @RequestParam(name = "deletedAt", required = false, defaultValue = "") String deletedAt,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<BookDto> books = profileService.getUserBooks(pageNo, pageSize, category, query, sortBy, orderBy, deletedAt, userDetails)
                .map(BookDto::new);

        return ResponseEntity.ok()
                .body(books);
    }
}
