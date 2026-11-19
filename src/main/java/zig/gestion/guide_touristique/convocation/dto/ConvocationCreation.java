package zig.gestion.guide_touristique.convocation.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConvocationCreation(
        @NotNull Instant dateHeure,
        @NotBlank String lieu,
        @Size(max = 2000) String instructions,
        Instant dateLimiteConfirmation) {
}
