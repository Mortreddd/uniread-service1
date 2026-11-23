package com.uniread.common.providers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public CustomUserDetails get() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }

        return null;
    }
}
