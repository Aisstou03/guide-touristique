package zig.gestion.guide_touristique.cv.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Certification(
        @Column(nullable = false) String intitule,
        @Column(nullable = false) String organisme,
        LocalDate dateObtention,
        String reference) {
}