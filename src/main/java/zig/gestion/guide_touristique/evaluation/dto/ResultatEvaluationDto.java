package zig.gestion.guide_touristique.evaluation.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import zig.gestion.guide_touristique.evaluation.DecisionEvaluation;
import zig.gestion.guide_touristique.evaluation.StatutPublication;

public record ResultatEvaluationDto(Long id, Long candidatureId, Long evaluateurId,
                                   List<NoteEvaluationSaisie> notes, DecisionEvaluation decision,
                                   String appreciation, Instant dateEvaluation, BigDecimal noteTotale,
                                   StatutPublication statutPublication) {
}
