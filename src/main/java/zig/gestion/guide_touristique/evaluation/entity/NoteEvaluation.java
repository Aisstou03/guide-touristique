package zig.gestion.guide_touristique.evaluation.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record NoteEvaluation(
        @Column(nullable = false) String critere,
        @Column(nullable = false, precision = 8, scale = 2) BigDecimal note,
        @Column(nullable = false, precision = 8, scale = 2) BigDecimal noteMaximale) {
}
