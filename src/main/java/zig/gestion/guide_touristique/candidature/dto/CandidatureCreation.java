package zig.gestion.guide_touristique.candidature.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Schéma « CandidatureCreation » du contrat. */
public record CandidatureCreation(
        @NotNull @Min(1) Long sessionId,
        @NotNull(message = "La confirmation d'exactitude est obligatoire.")
        @AssertTrue(message = "Le candidat doit confirmer l'exactitude des informations.")
        Boolean confirmationExactitude,
        @Size(max = 2000) String commentaire) {

    public CandidatureCreation {
        // Un commentaire vide ou fait d'espaces est considéré comme absent
        commentaire = (commentaire == null || commentaire.isBlank()) ? null : commentaire.trim();
    }
}