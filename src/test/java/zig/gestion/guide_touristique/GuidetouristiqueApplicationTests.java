package zig.gestion.guide_touristique;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import zig.gestion.guide_touristique.candidature.StatutCandidature;
import zig.gestion.guide_touristique.candidature.dto.CandidatureCreation;
import zig.gestion.guide_touristique.convocation.dto.ConvocationCreation;
import zig.gestion.guide_touristique.candidature.service.CandidatureService;
import zig.gestion.guide_touristique.convocation.service.ConvocationService;
import zig.gestion.guide_touristique.common.error.ConflitException;
import zig.gestion.guide_touristique.common.error.RequeteInvalideException;
import zig.gestion.guide_touristique.common.error.RessourceIntrouvableException;
import zig.gestion.guide_touristique.cv.dto.CvSaisie;
import zig.gestion.guide_touristique.cv.dto.ExperienceDto;
import zig.gestion.guide_touristique.cv.dto.FormationDto;
import zig.gestion.guide_touristique.cv.dto.LangueDto;
import zig.gestion.guide_touristique.cv.entity.NiveauLangue;
import zig.gestion.guide_touristique.cv.service.CvService;
import zig.gestion.guide_touristique.evaluation.DecisionEvaluation;
import zig.gestion.guide_touristique.evaluation.StatutPublication;
import zig.gestion.guide_touristique.evaluation.dto.NoteEvaluationSaisie;
import zig.gestion.guide_touristique.evaluation.dto.ResultatEvaluationSaisie;
import zig.gestion.guide_touristique.evaluation.service.ResultatEvaluationService;
import zig.gestion.guide_touristique.guide.StatutGuide;
import zig.gestion.guide_touristique.guide.dto.GuideCreation;
import zig.gestion.guide_touristique.guide.dto.GuideDto;
import zig.gestion.guide_touristique.guide.service.GuideService;
import zig.gestion.guide_touristique.session.dto.SessionCreation;
import zig.gestion.guide_touristique.session.service.SessionService;

@SpringBootTest
@Transactional
class ConvocationEtResultatIntegrationTest {
    @Autowired GuideService guideService;
    @Autowired CvService cvService;
    @Autowired SessionService sessionService;
    @Autowired CandidatureService candidatureService;
    @Autowired ConvocationService convocationService;
    @Autowired ResultatEvaluationService resultatService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void parcoursCompletConvocationEvaluationPublicationEtConsultationCandidat() {
        GuideDto guide = creerGuideComplet("guide-part3@example.test");
        var session = sessionService.creer(new SessionCreation("Examen Partie 3", "Dakar", "Centre",
                Instant.parse("2030-08-01T09:00:00Z"), Instant.parse("2030-08-01T17:00:00Z"),
                10, null, List.of()));
        var candidature = candidatureService.soumettre(guide.id(),
                new CandidatureCreation(session.id(), true, null));

        var convocation = convocationService.convoquer(candidature.id(),
                new ConvocationCreation(Instant.parse("2030-08-01T09:00:00Z"), "Centre de Dakar",
                        "Arriver 30 minutes en avance", Instant.parse("2030-07-25T23:59:00Z")));
        assertEquals("ENVOYEE", convocation.statut().name());
        assertEquals(StatutCandidature.CONVOQUEE, candidatureService.consulter(candidature.id()).statut());

        ResultatEvaluationSaisie saisie = new ResultatEvaluationSaisie(7L, List.of(
                new NoteEvaluationSaisie("Patrimoine", new BigDecimal("8"), new BigDecimal("10")),
                new NoteEvaluationSaisie("Mise en situation", new BigDecimal("4"), new BigDecimal("5"))),
                DecisionEvaluation.ADMIS, "Très bonne maîtrise du sujet.");
        var brouillon = resultatService.enregistrer(candidature.id(), saisie);

        assertEquals(new BigDecimal("12"), brouillon.noteTotale());
        assertEquals(StatutPublication.BROUILLON, brouillon.statutPublication());
        assertEquals(StatutCandidature.ADMISE, candidatureService.consulter(candidature.id()).statut());
        assertEquals(StatutGuide.ADMIS, guideService.consulter(guide.id()).statut());
        assertEquals(StatutPublication.BROUILLON, resultatService.consulter(candidature.id()).statutPublication());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                String.valueOf(guide.id()), "test", List.of(new SimpleGrantedAuthority("ROLE_CANDIDAT"))));
        assertThrows(RessourceIntrouvableException.class, () -> resultatService.consulter(candidature.id()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                "admin", "test", List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        resultatService.publier(candidature.id());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                String.valueOf(guide.id()), "test", List.of(new SimpleGrantedAuthority("ROLE_CANDIDAT"))));
        assertEquals(StatutPublication.PUBLIE, resultatService.consulter(candidature.id()).statutPublication());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                "999999", "test", List.of(new SimpleGrantedAuthority("ROLE_CANDIDAT"))));
        assertThrows(AccessDeniedException.class, () -> resultatService.consulter(candidature.id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void refuseConvocationSiLaCandidatureNestPasSoumiseEtRefuseNotesIncoherentes() {
        GuideDto guide = creerGuideComplet("guide-invalide@example.test");
        var session = sessionService.creer(new SessionCreation("Examen Partie 3 bis", "Dakar", "Centre",
                Instant.parse("2030-09-01T09:00:00Z"), Instant.parse("2030-09-01T17:00:00Z"),
                10, null, List.of()));
        var candidature = candidatureService.soumettre(guide.id(), new CandidatureCreation(session.id(), true, null));
        convocationService.convoquer(candidature.id(), new ConvocationCreation(
                Instant.parse("2030-09-01T09:00:00Z"), "Centre", null, null));

        assertThrows(ConflitException.class, () -> convocationService.convoquer(candidature.id(),
                new ConvocationCreation(Instant.parse("2030-09-02T09:00:00Z"), "Autre lieu", null, null)));
        assertThrows(RequeteInvalideException.class, () -> resultatService.enregistrer(candidature.id(),
                new ResultatEvaluationSaisie(7L, List.of(new NoteEvaluationSaisie("Patrimoine",
                        new BigDecimal("11"), new BigDecimal("10"))), DecisionEvaluation.ADMIS, null)));
        assertThrows(RessourceIntrouvableException.class,
                () -> resultatService.consulter(999999L));
    }

    private GuideDto creerGuideComplet(String email) {
        GuideDto guide = guideService.creer(new GuideCreation("Awa", "Diop", email, "770000000",
                "Dakar", "Dakar", null, List.of("Patrimoine")));
        cvService.enregistrer(guide.id(), new CvSaisie("Guide touristique", "Profil",
                List.of(new FormationDto("Tourisme", "Université", 2020, 2022, null, true)),
                List.of(new ExperienceDto("Guide", "Agence", LocalDate.of(2022, 1, 1), null,
                        true, "Visites", "Dakar")),
                List.of(new LangueDto("Français", NiveauLangue.COURANT)), List.of(), List.of(), List.of()));
        return guide;
    }
}
