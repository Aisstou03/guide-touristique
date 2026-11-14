package zig.gestion.guide_touristique.guide.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zig.gestion.guide_touristique.common.error.ConflitException;
import zig.gestion.guide_touristique.common.error.RessourceIntrouvableException;
import zig.gestion.guide_touristique.guide.StatutGuide;
import zig.gestion.guide_touristique.guide.dto.GuideCreation;
import zig.gestion.guide_touristique.guide.dto.GuideDto;
import zig.gestion.guide_touristique.guide.dto.GuideModification;
import zig.gestion.guide_touristique.guide.entity.Guide;
import zig.gestion.guide_touristique.guide.mapper.GuideMapper;
import zig.gestion.guide_touristique.guide.repository.GuideRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import zig.gestion.guide_touristique.guide.repository.GuideSpecifications;
@Service
@Transactional(readOnly = true)   // par défaut : lecture seule

public class GuideService {
    private final GuideRepository guideRepository;
    private final GuideMapper guideMapper;

    public GuideService(GuideRepository guideRepository, GuideMapper guideMapper) {
        this.guideRepository = guideRepository;
        this.guideMapper = guideMapper;
    }

    @Transactional   // écriture : on redéfinit la transaction
    public GuideDto creer(GuideCreation creation) {
        if (guideRepository.existsByEmailIgnoreCase(creation.email())) {
            throw new ConflitException("Un guide avec cet email existe déjà.");
        }
        Guide guide = guideMapper.toEntity(creation);
        guide.setStatut(StatutGuide.BROUILLON);   // règle du sujet : BROUILLON à la création
        return guideMapper.toDto(guideRepository.save(guide));
    }

    public Page<GuideDto> rechercher(String nom, String region, StatutGuide statut, int page, int taille) {
    // Tri fixe : sans lui, deux pages successives pourraient se chevaucher
    PageRequest pageable = PageRequest.of(page, taille, Sort.by("nom", "prenom", "id"));
    return guideRepository
            .findAll(GuideSpecifications.recherche(nom, region, statut), pageable)
            .map(guideMapper::toDto);
}

    public GuideDto consulter(Long id) {
        return guideMapper.toDto(trouver(id));
    }

    @Transactional
    public GuideDto modifier(Long id, GuideModification modification) {
        Guide guide = trouver(id);
        if (guideRepository.existsByEmailIgnoreCaseAndIdNot(modification.email(), id)) {
            throw new ConflitException("Un autre guide utilise déjà cet email.");
        }
        guideMapper.update(modification, guide);
        return guideMapper.toDto(guideRepository.save(guide));
    }

    @Transactional
    public void supprimer(Long id) {
        Guide guide = trouver(id);
        if (guide.getStatut() != StatutGuide.BROUILLON) {
            throw new ConflitException("Le profil ne peut plus être supprimé : le dossier est soumis.");
        }
        guideRepository.delete(guide);
    }

    private Guide trouver(Long id) {
        return guideRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Aucun guide trouvé pour cet identifiant."));
    }
}
