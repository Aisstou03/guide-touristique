package zig.gestion.guide_touristique.guide.dto;

import zig.gestion.guide_touristique.guide.StatutGuide;
import java.util.List;

public record GuideDto(Long id,
        String prenom,
        String nom,
        String email,
        String telephone,
        String photoUrl,
        String region,
        String ville,
        String adresseProfessionnelle,
        List<String> specialites,
        StatutGuide statut) {

}
