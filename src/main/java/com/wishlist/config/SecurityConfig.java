package com.wishlist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.wishlist.security.JWTConverter;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorizeRequests -> 
                authorizeRequests
                    .requestMatchers(antMatcher("/wishlist/public")).permitAll()  // Permitir acesso público ao endpoint /products/public
                    .requestMatchers(antMatcher("/swagger-ui.html")).permitAll()  // Permitir acesso público ao endpoint /products/public
                    .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()  // Permitir acesso público ao endpoint /products/public
                    .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()  // Permitir acesso público ao endpoint /products/public
                    .requestMatchers(antMatcher("/swagger-ui/index.html")).permitAll()  // Permitir acesso público ao endpoint /products/public
                    .requestMatchers(antMatcher("/swagger-resources/**")).permitAll()  // Permitir acesso público ao endpoint /products/public
                    .anyRequest().authenticated()                                 // Exigir autenticação para todos os outros endpoints
            )
            .oauth2ResourceServer(oauth2 -> 
                oauth2.jwt(jwt -> 
                    jwt.jwtAuthenticationConverter(new JWTConverter())
                )
            );

        return http.build();
    }
}
