package zig.gestion.guide_touristique.candidature.web;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zig.gestion.guide_touristique.candidature.dto.CandidatureCreation;
import zig.gestion.guide_touristique.candidature.dto.CandidatureDto;
import zig.gestion.guide_touristique.candidature.service.CandidatureService;

@RestController
@RequestMapping("/api/v1")
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    /** soumettreCandidature -> 201 */
    @PostMapping("/guides/{guideId}/candidatures")
    public ResponseEntity<CandidatureDto> soumettre(@PathVariable @Min(1) Long guideId,
                                                    @Valid @RequestBody CandidatureCreation creation) {
        CandidatureDto candidature = candidatureService.soumettre(guideId, creation);
        return ResponseEntity.created(URI.create("/api/v1/candidatures/" + candidature.id())).body(candidature);
    }

    /** listerCandidaturesGuide -> 200 */
    @GetMapping("/guides/{guideId}/candidatures")
    public List<CandidatureDto> lister(@PathVariable @Min(1) Long guideId) {
        return candidatureService.listerParGuide(guideId);
    }

    /** consulterCandidature -> 200 ou 404 */
    @GetMapping("/candidatures/{candidatureId}")
    public CandidatureDto consulter(@PathVariable @Min(1) Long candidatureId) {
        return candidatureService.consulter(candidatureId);
    }
}