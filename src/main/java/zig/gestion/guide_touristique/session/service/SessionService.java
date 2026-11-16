package zig.gestion.guide_touristique.session.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zig.gestion.guide_touristique.common.error.ConflitException;
import zig.gestion.guide_touristique.common.error.RequeteInvalideException;
import zig.gestion.guide_touristique.session.StatutSession;
import zig.gestion.guide_touristique.session.dto.SessionCreation;
import zig.gestion.guide_touristique.session.dto.SessionDto;
import zig.gestion.guide_touristique.session.entity.SessionEvaluation;
import zig.gestion.guide_touristique.session.mapper.SessionMapper;
import zig.gestion.guide_touristique.session.repository.SessionRepository;
import zig.gestion.guide_touristique.session.repository.SessionSpecifications;

@Service
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    public SessionService(SessionRepository sessionRepository, SessionMapper sessionMapper) {
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
    }

    @Transactional
    public SessionDto creer(SessionCreation creation) {
        // Règle du sujet : dateFin postérieure à dateDebut (400)
        if (!creation.dateFin().isAfter(creation.dateDebut())) {
            throw new RequeteInvalideException("La date de fin doit être postérieure à la date de début.");
        }
        // Session identique déjà existante (409)
        if (sessionRepository.existsByIntituleIgnoreCaseAndRegionIgnoreCaseAndDateDebut(
                creation.intitule(), creation.region(), creation.dateDebut())) {
            throw new ConflitException("Une session identique existe déjà.");
        }
        SessionEvaluation session = sessionMapper.toEntity(creation);
        session.setStatut(StatutSession.OUVERTE);   // hypothèse : créée directement ouverte
        return sessionMapper.toDto(sessionRepository.save(session));
    }

    public List<SessionDto> rechercher(String region, StatutSession statut) {
        return sessionRepository
                .findAll(SessionSpecifications.recherche(region, statut), Sort.by("dateDebut", "id"))
                .stream()
                .map(sessionMapper::toDto)
                .toList();
    }
}