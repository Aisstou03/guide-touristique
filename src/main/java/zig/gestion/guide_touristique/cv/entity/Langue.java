package zig.gestion.guide_touristique.cv.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


@Embeddable
public record Langue(
        @Column(nullable = false) String langue,
        @Enumerated(EnumType.STRING) @Column(nullable = false) NiveauLangue niveau) {
}
