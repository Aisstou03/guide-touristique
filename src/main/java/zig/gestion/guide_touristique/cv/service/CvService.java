package zig.gestion.guide_touristique.cv.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zig.gestion.guide_touristique.common.error.ConflitException;
import zig.gestion.guide_touristique.common.error.RessourceIntrouvableException;
import zig.gestion.guide_touristique.cv.dto.CvDto;
import zig.gestion.guide_touristique.cv.dto.CvSaisie;
import zig.gestion.guide_touristique.cv.entity.Cv;
import zig.gestion.guide_touristique.cv.mapper.CvMapper;
import zig.gestion.guide_touristique.cv.repository.CvRepository;
import zig.gestion.guide_touristique.guide.StatutGuide;
import zig.gestion.guide_touristique.guide.entity.Guide;
import zig.gestion.guide_touristique.guide.repository.GuideRepository;

@Service
@Transactional(readOnly = true)
public class CvService {

    private final GuideRepository guideRepository;
    private final CvRepository cvRepository;
    private final CvMapper cvMapper;
    private final CvPdfGenerator pdfGenerator;

    public CvService(GuideRepository guideRepository, CvRepository cvRepository,
                     CvMapper cvMapper, CvPdfGenerator pdfGenerator) {
        this.guideRepository = guideRepository;
        this.cvRepository = cvRepository;
        this.cvMapper = cvMapper;
        this.pdfGenerator = pdfGenerator;
    }

    public CvDto consulter(Long guideId) {
        trouverGuide(guideId);   // 404 si le guide n'existe pas
        Cv cv = cvRepository.findByGuideId(guideId)
                .orElseThrow(() -> new RessourceIntrouvableException("Aucun CV trouvé pour ce guide."));
        return cvMapper.toDto(cv);
    }

    /** Création OU mise à jour : la version augmente à chaque enregistrement. */
    @Transactional
    public CvDto enregistrer(Long guideId, CvSaisie saisie) {
        Guide guide = trouverGuide(guideId);

        // Règle du sujet : plus de modification dès que le dossier est soumis (409)
        if (guide.getStatut() != StatutGuide.BROUILLON) {
            throw new ConflitException("Le CV ne peut plus être modifié après soumission.");
        }

        Cv cv = cvRepository.findByGuideId(guideId).orElseGet(() -> {
            Cv nouveau = new Cv();
            nouveau.setGuide(guide);
            nouveau.setVersion(0);
            return nouveau;
        });

        cvMapper.updateEntity(saisie, cv);
        cv.setVersion(cv.getVersion() + 1);
        cv.setDateMiseAJour(Instant.now());

        return cvMapper.toDto(cvRepository.save(cv));
    }

    /** Génère le PDF du CV : 404 si le guide est inconnu, 409 si le CV n'existe pas. */
    public byte[] exporter(Long guideId) {
        Guide guide = trouverGuide(guideId);
        Cv cv = cvRepository.findByGuideId(guideId)
                .orElseThrow(() -> new ConflitException("Le CV n'existe pas : impossible de générer le PDF."));
        return pdfGenerator.generer(guide, cv);
    }

    private Guide trouverGuide(Long guideId) {
        return guideRepository.findById(guideId)
                .orElseThrow(() -> new RessourceIntrouvableException("Aucun guide trouvé pour cet identifiant."));
    }
}