package zig.gestion.guide_touristique.convocation.dto;

import java.time.Instant;

import zig.gestion.guide_touristique.convocation.StatutConvocation;

public record ConvocationDto(Long id, Long candidatureId, Instant dateHeure, String lieu,
                             String instructions, Instant dateLimiteConfirmation, StatutConvocation statut) {
}
