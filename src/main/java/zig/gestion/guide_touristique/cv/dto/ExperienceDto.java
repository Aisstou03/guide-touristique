package zig.gestion.guide_touristique.cv.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ExperienceDto(
        @NotBlank String poste,
        @NotBlank String organisme,
        @NotNull LocalDate dateDebut,
        LocalDate dateFin,
        Boolean enCours,
        @Size(max = 3000) String description,
        String region) {
}