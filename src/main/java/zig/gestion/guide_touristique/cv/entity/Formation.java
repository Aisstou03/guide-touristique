package zig.gestion.guide_touristique.cv.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Formation(@Column(nullable = false) String intitule,
        @Column(nullable = false) String etablissement,
        @Column(nullable = false) Integer anneeDebut,
        Integer anneeFin,
        @Column(length = 3000) String description,
        boolean diplomeObtenu) {

}
