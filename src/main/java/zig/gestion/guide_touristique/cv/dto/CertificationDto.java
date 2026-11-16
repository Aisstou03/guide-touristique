package zig.gestion.guide_touristique.cv.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record CertificationDto(
        @NotBlank String intitule,
        @NotBlank String organisme,
        LocalDate dateObtention,
        String reference) {
}