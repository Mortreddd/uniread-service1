package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.application.services.auth.JsonWebTokenService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiEndpoints.USERS)
@RequiredArgsConstructor
public class UserController {

    private final JsonWebTokenService jsonWebTokenService;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(name = "query", required = false) String query
    ) {
        Page<UserDto> users = userService.getUsers(pageNo, pageSize, query).map(UserDto::new);
        return ResponseEntity.ok()
                .body(users);
    }

    /**
     * Extract the user based on access token or jwt token of the user
     * @param authorizationHeader extracts the jwt token
     * @return User
     */
    @GetMapping(path = "/current")
    public ResponseEntity<UserDto> getCurrentUser(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String accessToken = authorizationHeader.substring(7);
        User user = jsonWebTokenService.getUser(accessToken);
        UserDto currentUser = new UserDto(user);
        return ResponseEntity.ok()
                .body(currentUser);

    }
}
