package zig.gestion.guide_touristique.session.dto;

import java.time.Instant;
import java.util.List;

import zig.gestion.guide_touristique.session.StatutSession;

/** Schéma « SessionEvaluation » du contrat. */
public record SessionDto(
        Long id,
        String intitule,
        String region,
        String lieu,
        Instant dateDebut,
        Instant dateFin,
        int capacite,
        String description,
        List<String> epreuves,
        StatutSession statut) {
}