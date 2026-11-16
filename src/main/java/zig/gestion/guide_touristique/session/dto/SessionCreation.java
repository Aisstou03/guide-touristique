package zig.gestion.guide_touristique.session.dto;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Schéma « SessionEvaluationCreation » du contrat. */
public record SessionCreation(
        @NotBlank String intitule,
        @NotBlank String region,
        String lieu,
        @NotNull Instant dateDebut,
        @NotNull Instant dateFin,
        @NotNull @Min(1) Integer capacite,
        @Size(max = 2000) String description,
        List<String> epreuves) {

    public SessionCreation {
        epreuves = epreuves == null ? List.of() : List.copyOf(epreuves);
    }
}