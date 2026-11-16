package zig.gestion.guide_touristique.cv.entity;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Experience( @Column(nullable = false) String poste,
        @Column(nullable = false) String organisme,
        @Column(nullable = false) LocalDate dateDebut,
        LocalDate dateFin,
        boolean enCours,
        @Column(length = 3000) String description,
        String region) {

}
