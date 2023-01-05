package com.notelysia.newsandroidservices.auth;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {

  @Value("${newsapp.http.auth-token-header-name}")
  private String principalRequestHeader;

  @Value("${newsapp.http.auth-token}")
  private String principalRequestValue;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    ApiKeyAuthFilter filter = new ApiKeyAuthFilter(principalRequestHeader);
    filter.setAuthenticationManager(
        authentication -> {
          String principal = (String) authentication.getPrincipal();
          if (!Objects.equals(principalRequestValue, principal)) {
            throw new BadCredentialsException(
                "The API key was not found or not the expected value.");
          }
          authentication.setAuthenticated(true);
          return authentication;
        });

    http.authorizeHttpRequests((request -> request.requestMatchers("/guest/newsdetails").authenticated()))
        .addFilter(filter)
        .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((request -> request.requestMatchers("/newssource").authenticated()))
            .addFilter(filter)
            .sessionManagement(
            sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}