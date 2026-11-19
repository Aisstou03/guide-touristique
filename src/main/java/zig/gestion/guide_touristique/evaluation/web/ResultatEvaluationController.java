package zig.gestion.guide_touristique.evaluation.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zig.gestion.guide_touristique.evaluation.dto.ResultatEvaluationDto;
import zig.gestion.guide_touristique.evaluation.dto.ResultatEvaluationSaisie;
import zig.gestion.guide_touristique.evaluation.service.ResultatEvaluationService;

@RestController
@RequestMapping("/api/v1/candidatures/{candidatureId}/resultats")
public class ResultatEvaluationController {
    private final ResultatEvaluationService resultatService;

    public ResultatEvaluationController(ResultatEvaluationService resultatService) {
        this.resultatService = resultatService;
    }

    @PostMapping
    public ResponseEntity<ResultatEvaluationDto> enregistrer(
            @PathVariable @Min(1) Long candidatureId,
            @Valid @RequestBody ResultatEvaluationSaisie saisie) {
        return ResponseEntity.status(201).body(resultatService.enregistrer(candidatureId, saisie));
    }

    @PostMapping("/publication")
    public ResultatEvaluationDto publier(@PathVariable @Min(1) Long candidatureId) {
        return resultatService.publier(candidatureId);
    }

    @GetMapping
    public ResultatEvaluationDto consulter(@PathVariable @Min(1) Long candidatureId) {
        return resultatService.consulter(candidatureId);
    }
}
