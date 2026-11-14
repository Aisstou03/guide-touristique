package zig.gestion.guide_touristique.common.error;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class GestionnaireErreursGlobal {
    private static final Logger LOG = LoggerFactory.getLogger(GestionnaireErreursGlobal.class);

    // --- Nos exceptions métier ---

    @ExceptionHandler(RessourceIntrouvableException.class)
    ResponseEntity<Erreur> introuvable(RessourceIntrouvableException ex) {
        return reponse(HttpStatus.NOT_FOUND, "RESSOURCE_INTROUVABLE", ex.getMessage(), null);
    }

    @ExceptionHandler(ConflitException.class)
    ResponseEntity<Erreur> conflit(ConflitException ex) {
        return reponse(HttpStatus.CONFLICT, "CONFLIT", ex.getMessage(), null);
    }

    @ExceptionHandler(RequeteInvalideException.class)
    ResponseEntity<Erreur> requeteInvalide(RequeteInvalideException ex) {
        return reponse(HttpStatus.BAD_REQUEST, "REQUETE_INVALIDE", ex.getMessage(), null);
    }

    // --- Erreurs de validation (400) ---

    /** Corps JSON invalide (@Valid) : champ manquant, email incorrect... */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Erreur> corpsInvalide(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        String message = ex.getBindingResult().getFieldErrors().size() == 1
                ? ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage()
                : "Les données de la requête sont invalides.";
        return reponse(HttpStatus.BAD_REQUEST, "REQUETE_INVALIDE", message, details);
    }

    /** Paramètres d'URL invalides : taille=101, page=-1, id=0... */
    @ExceptionHandler(HandlerMethodValidationException.class)
    ResponseEntity<Erreur> parametresInvalides(HandlerMethodValidationException ex) {
        String details = ex.getParameterValidationResults().stream()
                .flatMap(r -> r.getResolvableErrors().stream()
                        .map(e -> r.getMethodParameter().getParameterName() + " : " + e.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return reponse(HttpStatus.BAD_REQUEST, "REQUETE_INVALIDE", "Paramètres de requête invalides.", details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Erreur> contraintes(ConstraintViolationException ex) {
        return reponse(HttpStatus.BAD_REQUEST, "REQUETE_INVALIDE", "Paramètres de requête invalides.", ex.getMessage());
    }

    /** JSON mal formé, champ inconnu, valeur d'énumération inconnue... */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Erreur> corpsIllisible(HttpMessageNotReadableException ex) {
        return reponse(HttpStatus.BAD_REQUEST, "REQUETE_INVALIDE",
                "Le corps de la requête est illisible ou invalide.", ex.getMostSpecificCause().getMessage());
    }

    /** Ex. ?statut=N_IMPORTE_QUOI ou /guides/abc */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<Erreur> typeInvalide(MethodArgumentTypeMismatchException ex) {
        return reponse(HttpStatus.BAD_REQUEST, "REQUETE_INVALIDE",
                "Valeur invalide pour le paramètre '" + ex.getName() + "'.", null);
    }

    // --- Filet de sécurité ---

    /** Erreurs Spring standard (404 d'URL inconnue, 405...) puis erreurs inattendues (500). */
    @ExceptionHandler(Exception.class)
    ResponseEntity<Erreur> inattendue(Exception ex) {
        if (ex instanceof ErrorResponse er) {
            HttpStatusCode statut = er.getStatusCode();
            String code = statut.value() == 404 ? "RESSOURCE_INTROUVABLE" : "REQUETE_INVALIDE";
            String detail = er.getBody().getDetail();
            return reponse(statut, code, detail != null ? detail : "Requête invalide.", null);
        }
        LOG.error("Erreur inattendue", ex);
        return reponse(HttpStatus.INTERNAL_SERVER_ERROR, "ERREUR_INTERNE", "Une erreur interne est survenue.", null);
    }

    private static ResponseEntity<Erreur> reponse(HttpStatusCode statut, String code, String message, String details) {
        return ResponseEntity.status(statut)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new Erreur(code, message, details));
    }
}
