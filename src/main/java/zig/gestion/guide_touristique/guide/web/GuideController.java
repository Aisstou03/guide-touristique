package zig.gestion.guide_touristique.guide.web;
import java.net.URI;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import zig.gestion.guide_touristique.guide.dto.GuideCreation;
import zig.gestion.guide_touristique.guide.dto.GuideDto;
import zig.gestion.guide_touristique.guide.dto.GuideModification;
import zig.gestion.guide_touristique.guide.service.GuideService;
import java.util.List;

import jakarta.validation.constraints.Max;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import zig.gestion.guide_touristique.guide.StatutGuide;

@RestController
@RequestMapping("/api/v1/guides")

public class GuideController {
    private final GuideService guideService;

    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    /** operationId du contrat : creerGuide -> 201 */
    @PostMapping
    public ResponseEntity<GuideDto> creer(@Valid @RequestBody GuideCreation creation) {
        GuideDto guide = guideService.creer(creation);
        return ResponseEntity.created(URI.create("/api/v1/guides/" + guide.id())).body(guide);
    }
        /** rechercherGuides -> 200 (le total est dans l'en-tête X-Total-Count) */
    @GetMapping
    public ResponseEntity<List<GuideDto>> rechercher(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) StatutGuide statut,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int taille) {

        Page<GuideDto> resultat = guideService.rechercher(nom, region, statut, page, taille);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(resultat.getTotalElements()))
                .body(resultat.getContent());
    }

    /** consulterGuide -> 200 */
    @GetMapping("/{guideId}")
    public GuideDto consulter(@PathVariable @Min(1) Long guideId) {
        return guideService.consulter(guideId);
    }

    /** modifierGuide -> 200 */
    @PutMapping("/{guideId}")
    public GuideDto modifier(@PathVariable @Min(1) Long guideId,
                             @Valid @RequestBody GuideModification modification) {
        return guideService.modifier(guideId, modification);
    }

    /** supprimerGuide -> 204 */
    @DeleteMapping("/{guideId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable @Min(1) Long guideId) {
        guideService.supprimer(guideId);
    }

}
