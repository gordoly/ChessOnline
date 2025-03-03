package com.example.Chess.Configuration.Jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Chess.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository userRepository;

    /**
     * Defines a Bean for UserDetailsService to load a user's data from the repository
     * based on the username. If the user is not found, it throws a UsernameNotFoundException.
     *
     * @return UserDetailsService lambda express that loads user details by email
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    /**
     * Defines a Bean for PasswordEncoder using BCryptPasswordEncoder for encoding passwords.
     *
     * @return PasswordEncoder implementation that uses BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines a Bean for AuthenticationManager which is used to authenticate users with an JWT token.
     *
     * @param config the AuthenticationConfiguration to configure the AuthenticationManager
     * @return AuthenticationManager for authenticating users
     * @throws Exception if there is an error retrieving the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

     /**
     * Defines a Bean for AuthenticationProvider, which will handle the authentication process.
     *
     * @return AuthenticationProvider configured with a UserDetailsService and PasswordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}