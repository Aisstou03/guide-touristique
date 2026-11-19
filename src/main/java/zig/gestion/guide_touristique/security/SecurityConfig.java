package zig.gestion.guide_touristique.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

/** Comptes de démonstration pour tester les accès de la Partie 3. */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/v1/sessions-evaluation").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/candidatures/*/convocation")
                    .hasAnyRole("GESTIONNAIRE", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/candidatures/*/resultats/publication")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/candidatures/*/resultats")
                    .hasAnyRole("EVALUATEUR", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/candidatures/*/resultats").authenticated()
                .anyRequest().permitAll())
            .httpBasic(basic -> {})
            .exceptionHandling(errors -> errors
                .authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(401);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"code\":\"NON_AUTORISE\",\"message\":\"Une authentification valide est requise.\",\"details\":null}");
                })
                .accessDeniedHandler((request, response, exception) -> {
                    response.setStatus(403);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"code\":\"ACCES_REFUSE\",\"message\":\"Le rôle requis pour cette opération est absent.\",\"details\":null}");
                }));
        return http.build();
    }

    /** Comptes locaux de démonstration ; à remplacer par les identités réelles en Partie 4. */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            return switch (username) {
                case "admin" -> User.withUsername("admin").password("{noop}admin123").roles("ADMIN").build();
                case "gestionnaire" -> User.withUsername("gestionnaire").password("{noop}gestionnaire123")
                        .roles("GESTIONNAIRE").build();
                case "evaluateur" -> User.withUsername("evaluateur").password("{noop}evaluateur123")
                        .roles("EVALUATEUR").build();
                default -> {
                    // Pour la démonstration, le numéro du guide sert de login candidat.
                    if (!username.matches("[1-9][0-9]*")) throw new UsernameNotFoundException("Utilisateur inconnu");
                    yield User.withUsername(username).password("{noop}candidat123").roles("CANDIDAT").build();
                }
            };
        };
    }
}
