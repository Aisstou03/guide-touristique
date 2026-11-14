package zig.gestion.guide_touristique.guide.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GuideModification(
        @NotBlank @Size(max = 100) String prenom,
        @NotBlank @Size(max = 100) String nom,
        @NotBlank @Email String email,
        String telephone,
        @NotBlank String region,
        String ville,
        String adresseProfessionnelle,
        List<String> specialites) {

    public GuideModification {
        specialites = specialites == null ? List.of() : List.copyOf(specialites);
    }
}