package com.bsit.uniread.infrastructure.configurations;

import com.bsit.uniread.application.services.user.CustomUserDetailsService;
import com.bsit.uniread.infrastructure.security.JsonWebTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JsonWebTokenFilter jsonWebTokenFilter;
    private final ApplicationConfiguration applicationConfiguration;
    private final CustomUserDetailsService customUserDetailsService;

    private final String[] allowedEndpoints =  new String[]{
            "/api/v1/profile/*",
            "/api/v1/books",
            "/api/v1/books/*",
            "/api/v1/books/*/comments",
            "/api/v1/books/*/comments/*",
            "/api/v1/authors",
            "/api/v1/auth/**",
            "/oauth2/**",
            "/api/v1/genres/**",
            "/api/v1/genres/*/books",
            "/api/v1/genres/options",
            "/api/v1/messages/**",
            "/api/v1/users",
            "/api/v1/authors/**",
            "/messages/**",
            "/app/**",
            "/topic/**",
            "/queue/**",
            "/notifications/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(allowedEndpoints)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jsonWebTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .anonymous(AnonymousConfigurer::disable)
                .build();
    }

}
