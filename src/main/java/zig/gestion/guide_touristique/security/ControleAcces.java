package zig.gestion.guide_touristique.security;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class ControleAcces {
    public Authentication authentifie() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !a.isAuthenticated() || a instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException("Une authentification est requise.");
        }
        return a;
    }

    public void exigerRole(String... roles) {
        Authentication a = authentifie();
        boolean autorise = a.getAuthorities().stream()
                .anyMatch(authority -> Set.of(roles).contains(authority.getAuthority()));
        if (!autorise) throw new AccessDeniedException("Le rôle requis pour cette opération est absent.");
    }

    public boolean estEvaluateurOuAdmin() {
        Authentication a = authentifie();
        return a.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EVALUATEUR"))
                || a.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
