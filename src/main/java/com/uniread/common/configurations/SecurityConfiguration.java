package com.uniread.common.configurations;

import com.uniread.user.service.CustomUserDetailsService;
import com.uniread.auth.filters.JsonWebTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JsonWebTokenFilter jsonWebTokenFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    private final String[] allowedEndpoints =  new String[]{
            "/auth/**",
            "/profile/*",
            "/books",
            "/books/*",
            "/books/*/chapters",
            "/books/*/comments",
            "/books/*/comments/*",
            "/books/*/collaborators",
            "/authors",
            "/authors/*",
            "/oauth2/**",
            "/genres/**",
            "/genres/*/books",
            "/genres/options",
            "/messages/**",
            "/authors/**",
            "/users/*/follow/followings",
            "/users/*/follow/followers",
            "/ws/**",
            "/swagger-ui/**",
            "/v3/api-docs*/**"

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource.corsConfiguration()))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(allowedEndpoints)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication required\"}");
                        })
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jsonWebTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
