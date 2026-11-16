package zig.gestion.guide_touristique.session.web;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import zig.gestion.guide_touristique.session.StatutSession;
import zig.gestion.guide_touristique.session.dto.SessionCreation;
import zig.gestion.guide_touristique.session.dto.SessionDto;
import zig.gestion.guide_touristique.session.service.SessionService;

@RestController
@RequestMapping("/api/v1/sessions-evaluation")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /** creerSessionEvaluation -> 201 (réservé à un administrateur : Partie 4) */
    @PostMapping
    public ResponseEntity<SessionDto> creer(@Valid @RequestBody SessionCreation creation) {
        SessionDto session = sessionService.creer(creation);
        return ResponseEntity.created(URI.create("/api/v1/sessions-evaluation/" + session.id())).body(session);
    }

    /** rechercherSessionsEvaluation -> 200 */
    @GetMapping
    public List<SessionDto> rechercher(@RequestParam(required = false) String region,
                                       @RequestParam(required = false) StatutSession statut) {
        return sessionService.rechercher(region, statut);
    }
}