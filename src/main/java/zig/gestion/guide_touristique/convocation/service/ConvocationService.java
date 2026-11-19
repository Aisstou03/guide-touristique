package zig.gestion.guide_touristique.convocation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zig.gestion.guide_touristique.candidature.StatutCandidature;
import zig.gestion.guide_touristique.convocation.dto.ConvocationCreation;
import zig.gestion.guide_touristique.convocation.dto.ConvocationDto;
import zig.gestion.guide_touristique.candidature.entity.Candidature;
import zig.gestion.guide_touristique.convocation.entity.Convocation;
import zig.gestion.guide_touristique.candidature.repository.CandidatureRepository;
import zig.gestion.guide_touristique.convocation.repository.ConvocationRepository;
import zig.gestion.guide_touristique.common.error.ConflitException;
import zig.gestion.guide_touristique.common.error.RequeteInvalideException;
import zig.gestion.guide_touristique.common.error.RessourceIntrouvableException;
import zig.gestion.guide_touristique.security.ControleAcces;

@Service
@Transactional(readOnly = true)
public class ConvocationService {
    private final CandidatureRepository candidatureRepository;
    private final ConvocationRepository convocationRepository;
    private final ControleAcces controleAcces;

    public ConvocationService(CandidatureRepository candidatureRepository,
                              ConvocationRepository convocationRepository, ControleAcces controleAcces) {
        this.candidatureRepository = candidatureRepository;
        this.convocationRepository = convocationRepository;
        this.controleAcces = controleAcces;
    }

    @Transactional
    public ConvocationDto convoquer(Long candidatureId, ConvocationCreation creation) {
        controleAcces.exigerRole("ROLE_GESTIONNAIRE", "ROLE_ADMIN");
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RessourceIntrouvableException("Aucune candidature trouvée pour cet identifiant."));
        if (candidature.getStatut() != StatutCandidature.SOUMISE) {
            throw new ConflitException("La candidature doit être au statut SOUMISE pour être convoquée.");
        }
        if (convocationRepository.existsByCandidatureId(candidatureId)) {
            throw new ConflitException("Une convocation existe déjà pour cette candidature.");
        }
        if (creation.dateLimiteConfirmation() != null
                && creation.dateLimiteConfirmation().isAfter(creation.dateHeure())) {
            throw new RequeteInvalideException("La date limite de confirmation doit précéder la convocation.");
        }

        Convocation convocation = new Convocation();
        convocation.setCandidature(candidature);
        convocation.setDateHeure(creation.dateHeure());
        convocation.setLieu(creation.lieu());
        convocation.setInstructions(creation.instructions());
        convocation.setDateLimiteConfirmation(creation.dateLimiteConfirmation());
        candidature.setStatut(StatutCandidature.CONVOQUEE);
        Candidature saved = candidatureRepository.save(candidature);
        Convocation savedConvocation = convocationRepository.save(convocation);
        return new ConvocationDto(savedConvocation.getId(), saved.getId(), savedConvocation.getDateHeure(),
                savedConvocation.getLieu(), savedConvocation.getInstructions(),
                savedConvocation.getDateLimiteConfirmation(), savedConvocation.getStatut());
    }
}
