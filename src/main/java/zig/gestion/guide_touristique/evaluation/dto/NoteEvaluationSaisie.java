package zig.gestion.guide_touristique.evaluation.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NoteEvaluationSaisie(
        @NotBlank String critere,
        @NotNull @DecimalMin("0.00") BigDecimal note,
        @NotNull @DecimalMin(value = "0.01") BigDecimal noteMaximale) {
}
