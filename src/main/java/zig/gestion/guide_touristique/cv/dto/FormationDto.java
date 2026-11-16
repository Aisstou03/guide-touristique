package zig.gestion.guide_touristique.cv.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FormationDto(
        @NotBlank String intitule,
        @NotBlank String etablissement,
        @NotNull @Min(1950) Integer anneeDebut,
        Integer anneeFin,
        String description,
        Boolean diplomeObtenu) {
}