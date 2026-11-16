package zig.gestion.guide_touristique.cv.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Schéma « CVGuideSaisie » du contrat : le corps du PUT /cv. */
public record CvSaisie(
        @Size(max = 200) String titreProfessionnel,
        @Size(max = 3000) String profilProfessionnel,
        @NotNull @Valid List<FormationDto> formations,
        @NotNull @Valid List<ExperienceDto> experiences,
        @NotEmpty(message = "Le CV doit contenir au moins une langue.") @Valid List<LangueDto> langues,
        List<String> competences,
        @Valid List<CertificationDto> certifications,
        List<String> referencesProfessionnelles) {

    public CvSaisie {
        // Listes optionnelles : absentes du JSON -> liste vide.
        // Les 3 listes obligatoires restent validées (volontairement pas touchées ici).
        competences = competences == null ? List.of() : List.copyOf(competences);
        certifications = certifications == null ? List.of() : List.copyOf(certifications);
        referencesProfessionnelles = referencesProfessionnelles == null
                ? List.of() : List.copyOf(referencesProfessionnelles);
    }
}   