package zig.gestion.guide_touristique.cv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import zig.gestion.guide_touristique.cv.entity.NiveauLangue;

public record LangueDto(
        @NotBlank String langue,
        @NotNull NiveauLangue niveau) {
}
