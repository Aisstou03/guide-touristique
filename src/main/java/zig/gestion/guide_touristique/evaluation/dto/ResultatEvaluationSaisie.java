package zig.gestion.guide_touristique.evaluation.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import zig.gestion.guide_touristique.evaluation.DecisionEvaluation;

public record ResultatEvaluationSaisie(
        @NotNull @Min(1) Long evaluateurId,
        @NotEmpty @Valid List<NoteEvaluationSaisie> notes,
        @NotNull DecisionEvaluation decision,
        @Size(max = 3000) String appreciation) {
}
