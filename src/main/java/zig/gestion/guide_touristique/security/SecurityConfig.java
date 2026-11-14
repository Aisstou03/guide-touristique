package zig.gestion.guide_touristique.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * TEMPORAIRE (Parties 1 à 3) : tout est ouvert pour pouvoir tester l'API.
 * Remplacée en Partie 4 par JWT + rôles + contrôle de propriété.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
     @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
