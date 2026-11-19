package zig.gestion.guide_touristique.candidature.dto;

import java.time.Instant;

import zig.gestion.guide_touristique.candidature.StatutConvocation;

public record ConvocationDto(
        Long id,
        Long candidatureId,
        Instant dateHeure,
        String lieu,
        String instructions,
        Instant dateLimiteConfirmation,
        StatutConvocation statut) {
}