package zig.gestion.guide_touristique.candidature.dto;

import java.time.Instant;

import zig.gestion.guide_touristique.candidature.StatutCandidature;

/** Schéma « Candidature » du contrat. */
public record CandidatureDto(
        Long id,
        Long guideId,
        Long sessionId,
        Integer versionCV,
        StatutCandidature statut,
        Instant dateSoumission,
        String commentaire) {
}