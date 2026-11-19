package zig.gestion.guide_touristique.evaluation.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zig.gestion.guide_touristique.candidature.StatutCandidature;
import zig.gestion.guide_touristique.candidature.entity.Candidature;
import zig.gestion.guide_touristique.candidature.repository.CandidatureRepository;
import zig.gestion.guide_touristique.common.error.ConflitException;
import zig.gestion.guide_touristique.common.error.RequeteInvalideException;
import zig.gestion.guide_touristique.common.error.RessourceIntrouvableException;
import zig.gestion.guide_touristique.evaluation.DecisionEvaluation;
import zig.gestion.guide_touristique.evaluation.StatutPublication;
import zig.gestion.guide_touristique.evaluation.dto.NoteEvaluationSaisie;
import zig.gestion.guide_touristique.evaluation.dto.ResultatEvaluationDto;
import zig.gestion.guide_touristique.evaluation.dto.ResultatEvaluationSaisie;
import zig.gestion.guide_touristique.evaluation.entity.NoteEvaluation;
import zig.gestion.guide_touristique.evaluation.entity.ResultatEvaluation;
import zig.gestion.guide_touristique.evaluation.repository.ResultatEvaluationRepository;
import zig.gestion.guide_touristique.guide.StatutGuide;
import zig.gestion.guide_touristique.security.ControleAcces;

@Service
@Transactional(readOnly = true)
public class ResultatEvaluationService {
    private final CandidatureRepository candidatureRepository;
    private final ResultatEvaluationRepository resultatRepository;
    private final ControleAcces controleAcces;

    public ResultatEvaluationService(CandidatureRepository candidatureRepository,
            ResultatEvaluationRepository resultatRepository, ControleAcces controleAcces) {
        this.candidatureRepository = candidatureRepository;
        this.resultatRepository = resultatRepository;
        this.controleAcces = controleAcces;
    }

    @Transactional
    public ResultatEvaluationDto enregistrer(Long candidatureId, ResultatEvaluationSaisie saisie) {
        controleAcces.exigerRole("ROLE_EVALUATEUR", "ROLE_ADMIN");
        Candidature candidature = trouverCandidature(candidatureId);
        if (candidature.getStatut() != StatutCandidature.CONVOQUEE) {
            throw new ConflitException("La candidature doit être CONVOQUEE avant son évaluation.");
        }
        if (resultatRepository.findByCandidatureId(candidatureId).isPresent()) {
            throw new ConflitException("Un résultat existe déjà pour cette candidature.");
        }

        verifierCoherence(saisie.notes());
        List<NoteEvaluation> notes = saisie.notes().stream()
                .map(n -> new NoteEvaluation(n.critere().trim(), n.note(), n.noteMaximale())).toList();
        BigDecimal total = notes.stream().map(NoteEvaluation::note)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ResultatEvaluation resultat = new ResultatEvaluation();
        resultat.setCandidature(candidature);
        resultat.setEvaluateurId(saisie.evaluateurId());
        resultat.setNotes(notes);
        resultat.setDecision(saisie.decision());
        resultat.setAppreciation(saisie.appreciation());
        resultat.setDateEvaluation(Instant.now());
        resultat.setNoteTotale(total);

        appliquerDecision(candidature, saisie.decision());
        return versDto(resultatRepository.save(resultat));
    }

    @Transactional
    public ResultatEvaluationDto publier(Long candidatureId) {
        controleAcces.exigerRole("ROLE_ADMIN");
        ResultatEvaluation resultat = resultatRepository.findByCandidatureId(candidatureId)
                .orElseThrow(() -> new RessourceIntrouvableException("Aucun résultat trouvé pour cette candidature."));
        if (resultat.getStatutPublication() == StatutPublication.PUBLIE) {
            throw new ConflitException("Ce résultat est déjà publié.");
        }
        resultat.setStatutPublication(StatutPublication.PUBLIE);
        return versDto(resultatRepository.save(resultat));
    }

    public ResultatEvaluationDto consulter(Long candidatureId) {
        ResultatEvaluation resultat = resultatRepository.findByCandidatureId(candidatureId)
                .orElseThrow(() -> new RessourceIntrouvableException("Aucun résultat trouvé pour cette candidature."));
        Authentication utilisateur = controleAcces.authentifie();
        boolean habilite = utilisateur.getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("ROLE_EVALUATEUR") || a.getAuthority().equals("ROLE_ADMIN"));
        if (!habilite) {
            boolean estCandidat = utilisateur.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDAT"));
            boolean estSonDossier = utilisateur.getName().equals(String.valueOf(resultat.getCandidature().getGuide().getId()));
            if (!estCandidat || !estSonDossier) throw new AccessDeniedException("Ce résultat ne vous appartient pas.");
            if (resultat.getStatutPublication() != StatutPublication.PUBLIE) {
                throw new RessourceIntrouvableException("Aucun résultat publié n'est disponible pour cette candidature.");
            }
        }
        return versDto(resultat);
    }

    private void verifierCoherence(List<NoteEvaluationSaisie> notes) {
        if (notes == null || notes.isEmpty()) throw new RequeteInvalideException("Au moins une note est obligatoire.");
        HashSet<String> criteres = new HashSet<>();
        for (NoteEvaluationSaisie note : notes) {
            if (note.note().compareTo(note.noteMaximale()) > 0) {
                throw new RequeteInvalideException("Une note ne peut pas dépasser sa note maximale.");
            }
            if (!criteres.add(note.critere().trim().toLowerCase())) {
                throw new RequeteInvalideException("Un critère ne peut apparaître qu'une seule fois.");
            }
        }
    }

    private void appliquerDecision(Candidature candidature, DecisionEvaluation decision) {
        switch (decision) {
            case ADMIS -> {
                candidature.setStatut(StatutCandidature.ADMISE);
                candidature.getGuide().setStatut(StatutGuide.ADMIS);
            }
            case NON_ADMIS -> {
                candidature.setStatut(StatutCandidature.NON_ADMISE);
                candidature.getGuide().setStatut(StatutGuide.NON_ADMIS);
            }
            case AJOURNE -> {
                candidature.setStatut(StatutCandidature.EVALUEE);
                candidature.getGuide().setStatut(StatutGuide.EN_EVALUATION);
            }
        }
        candidatureRepository.save(candidature);
    }

    private Candidature trouverCandidature(Long id) {
        return candidatureRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Aucune candidature trouvée pour cet identifiant."));
    }

    private ResultatEvaluationDto versDto(ResultatEvaluation r) {
        List<NoteEvaluationSaisie> notes = r.getNotes().stream()
                .map(n -> new NoteEvaluationSaisie(n.critere(), n.note(), n.noteMaximale())).toList();
        return new ResultatEvaluationDto(r.getId(), r.getCandidature().getId(), r.getEvaluateurId(), notes,
                r.getDecision(), r.getAppreciation(), r.getDateEvaluation(), r.getNoteTotale(), r.getStatutPublication());
    }
}
