package com.zeus.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.zeus.models.User;
import com.zeus.repositories.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/css/**", "/images/**", "/auxshare", "/auxshare/logout").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/songs/**").permitAll()       // 👈 TILLÅT GET
                .requestMatchers(HttpMethod.POST, "/api/songs/**").authenticated()  // 👈 Begränsa POST
                .anyRequest().permitAll() // eller .permitAll() beroende på din önskade säkerhetsnivå
            )
            .formLogin(form -> form
                .loginPage("/auxshare/login")
                .defaultSuccessUrl("/auxshare", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/auxshare/login?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                            .withUsername(user.getUsername())
                            .password(user.getPassword())
                            .roles("USER")
                            .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
