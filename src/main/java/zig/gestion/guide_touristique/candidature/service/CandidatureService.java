package zig.gestion.guide_touristique.candidature.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import zig.gestion.guide_touristique.candidature.StatutCandidature;
import zig.gestion.guide_touristique.candidature.dto.CandidatureCreation;
import zig.gestion.guide_touristique.candidature.dto.CandidatureDto;
import zig.gestion.guide_touristique.candidature.entity.Candidature;
import zig.gestion.guide_touristique.candidature.entity.CvFige;
import zig.gestion.guide_touristique.candidature.mapper.CandidatureMapper;
import zig.gestion.guide_touristique.candidature.repository.CandidatureRepository;
import zig.gestion.guide_touristique.common.error.ConflitException;
import zig.gestion.guide_touristique.common.error.RequeteInvalideException;
import zig.gestion.guide_touristique.common.error.RessourceIntrouvableException;
import zig.gestion.guide_touristique.cv.entity.Cv;
import zig.gestion.guide_touristique.cv.repository.CvRepository;
import zig.gestion.guide_touristique.guide.StatutGuide;
import zig.gestion.guide_touristique.guide.entity.Guide;
import zig.gestion.guide_touristique.guide.repository.GuideRepository;
import zig.gestion.guide_touristique.session.StatutSession;
import zig.gestion.guide_touristique.session.entity.SessionEvaluation;
import zig.gestion.guide_touristique.session.repository.SessionRepository;

@Service
@Transactional(readOnly = true)
public class CandidatureService {

    private final GuideRepository guideRepository;
    private final SessionRepository sessionRepository;
    private final CvRepository cvRepository;
    private final CandidatureRepository candidatureRepository;
    private final CandidatureMapper candidatureMapper;

    public CandidatureService(GuideRepository guideRepository, SessionRepository sessionRepository,
                              CvRepository cvRepository, CandidatureRepository candidatureRepository,
                              CandidatureMapper candidatureMapper) {
        this.guideRepository = guideRepository;
        this.sessionRepository = sessionRepository;
        this.cvRepository = cvRepository;
        this.candidatureRepository = candidatureRepository;
        this.candidatureMapper = candidatureMapper;
    }

    @Transactional
    public CandidatureDto soumettre(Long guideId, CandidatureCreation creation) {
        Guide guide = trouverGuide(guideId);
        SessionEvaluation session = sessionRepository.findById(creation.sessionId())
                .orElseThrow(() -> new RessourceIntrouvableException("Aucune session trouvée pour cet identifiant."));

        // --- Règles de session (409) ---
        if (session.getStatut() != StatutSession.OUVERTE) {
            throw new ConflitException("La session n'est pas ouverte aux candidatures.");
        }
        if (candidatureRepository.existsByGuideIdAndSessionIdAndStatutNot(
                guideId, session.getId(), StatutCandidature.NON_ADMISE)) {
            throw new ConflitException("Ce guide a déjà une candidature active sur cette session.");
        }
        if (candidatureRepository.countBySessionIdAndStatutNot(
                session.getId(), StatutCandidature.NON_ADMISE) >= session.getCapacite()) {
            throw new ConflitException("La session est complète.");
        }
        if (guide.getStatut() != StatutGuide.BROUILLON) {
            throw new ConflitException("Le dossier a déjà été soumis.");
        }

        // --- Complétude du profil et du CV (400) ---
        Cv cv = cvRepository.findByGuideId(guideId).orElse(null);
        verifierCompletude(guide, cv);

        // --- Création : copie figée du CV + statut de la candidature ---
        Candidature candidature = new Candidature();
        candidature.setGuide(guide);
        candidature.setSession(session);
        candidature.setCommentaire(creation.commentaire());
        candidature.setStatut(StatutCandidature.SOUMISE);
        candidature.setDateSoumission(Instant.now());
        candidature.setCvFige(CvFige.depuis(cv));

        // --- Le guide passe à DOSSIER_SOUMIS : son CV n'est plus modifiable ---
        guide.setStatut(StatutGuide.DOSSIER_SOUMIS);
        guideRepository.save(guide);

        return candidatureMapper.toDto(candidatureRepository.save(candidature));
    }

    public List<CandidatureDto> listerParGuide(Long guideId) {
        trouverGuide(guideId);
        return candidatureRepository.findByGuideIdOrderByDateSoumissionDesc(guideId).stream()
                .map(candidatureMapper::toDto)
                .toList();
    }

    public CandidatureDto consulter(Long candidatureId) {
        return candidatureMapper.toDto(candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RessourceIntrouvableException("Aucune candidature trouvée pour cet identifiant.")));
    }

    private void verifierCompletude(Guide guide, Cv cv) {
        List<String> manques = new ArrayList<>();
        if (!StringUtils.hasText(guide.getTelephone())) {
            manques.add("téléphone du profil");
        }
        if (!StringUtils.hasText(guide.getVille())) {
            manques.add("ville du profil");
        }
        if (guide.getSpecialites().isEmpty()) {
            manques.add("au moins une spécialité du profil");
        }
        if (cv == null) {
            manques.add("CV inexistant");
        } else {
            if (!StringUtils.hasText(cv.getTitreProfessionnel())) {
                manques.add("titre professionnel du CV");
            }
            if (cv.getFormations().isEmpty()) {
                manques.add("au moins une formation dans le CV");
            }
            if (cv.getExperiences().isEmpty()) {
                manques.add("au moins une expérience dans le CV");
            }
            if (cv.getLangues().isEmpty()) {
                manques.add("au moins une langue dans le CV");
            }
        }
        if (!manques.isEmpty()) {
            throw new RequeteInvalideException("Dossier incomplet : " + String.join(", ", manques) + ".");
        }
    }

    private Guide trouverGuide(Long guideId) {
        return guideRepository.findById(guideId)
                .orElseThrow(() -> new RessourceIntrouvableException("Aucun guide trouvé pour cet identifiant."));
    }
}