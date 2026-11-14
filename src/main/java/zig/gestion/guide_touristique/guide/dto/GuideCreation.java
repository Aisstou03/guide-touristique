package zig.gestion.guide_touristique.guide.dto;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GuideCreation(  @NotBlank @Size(max = 100) String prenom,
    @NotBlank @Size(max = 100) String nom,
    @NotBlank @Email String email,
    String telephone,
    @NotBlank String region,
    String ville,
    String adresseProfessionnelle,
    List<String> specialites) {
  

    // Constructeur compact : si "specialites" est absent du JSON, on met une liste vide
    public GuideCreation {
        specialites = specialites == null ? List.of() : List.copyOf(specialites);
    }
}
