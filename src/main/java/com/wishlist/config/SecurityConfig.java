package com.wishlist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
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
                    .requestMatchers(antMatcher("/wishlist/public")).permitAll()
                    .requestMatchers(antMatcher("/swagger-ui.html")).permitAll()  
                    .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()  
                    .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
                    .requestMatchers(antMatcher("/swagger-ui/index.html")).permitAll()
                    .requestMatchers(antMatcher("/swagger-resources/**")).permitAll()
                    .anyRequest().authenticated() // Exigir autenticação para todos os outros endpoints
            )
            .oauth2ResourceServer(oauth2 -> 
                oauth2.jwt(jwt -> 
                    jwt.jwtAuthenticationConverter(new JWTConverter())
                )
            );

        return http.build();
    }

    /*@Bean
    @Profile("test")
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorizeRequests -> 
                authorizeRequests
                    .anyRequest().permitAll()
            );
        return http.build();
    }*/   

}
