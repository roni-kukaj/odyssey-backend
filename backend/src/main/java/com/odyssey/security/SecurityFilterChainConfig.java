package com.odyssey.security;

import com.odyssey.jwt.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityFilterChainConfig(
            AuthenticationProvider authenticationProvider,
            JWTAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/users",
                        "/api/v1/auth/login",
                        "/api/v1/subscribers"
                )
                .permitAll()
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/news",
                        "/api/v1/news/**",
                        "/api/v1/activities",
                        "/api/v1/activities/**",
                        "/api/v1/events",
                        "/api/v1/events/**",
                        "/api/v1/flights",
                        "/api/v1/flights/**",
                        "/api/v1/hotels",
                        "/api/v1/hotels/**",
                        "/api/v1/recommendations",
                        "/api/v1/recommendations/**",
                        "/api/v1/locations",
                        "/api/v1/locations/**",
                        "/api/v1/items",
                        "/api/v1/items/**"
                )
                .permitAll()
                .requestMatchers(
                        "/api-docs/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                )
                .permitAll()
//                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{userId}")
//                .hasAuthority("ADMIN")
                .anyRequest()
                .authenticated()
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(handler -> handler
                .authenticationEntryPoint(authenticationEntryPoint)
            );

        return http.build();
    }

}
