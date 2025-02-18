package org.team1.keyduck.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, SecurityContextHolderAwareRequestFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auctions**").permitAll()
                        .requestMatchers("/api/members**").hasAnyRole("SELLER", "CUSTOMER")
                        .requestMatchers("/api/keyboards**").hasRole("SELLER")
                        .requestMatchers("/api/payment**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/auctions").hasRole("SELLER")
                        .requestMatchers(HttpMethod.PATCH, "/api/auctions/*").hasRole("SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/biddings/*")
                        .hasAnyRole("CUSTOMER", "SELLER")
                        .requestMatchers(HttpMethod.POST, "/api/biddings/*").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/biddings/success")
                        .hasRole("CUSTOMER")
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .permitAll()
                        .requestMatchers("/payment_login.html").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}
