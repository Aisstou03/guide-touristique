package zig.gestion.guide_touristique.session.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import zig.gestion.guide_touristique.session.entity.SessionEvaluation;

public interface SessionRepository
        extends JpaRepository<SessionEvaluation, Long>, JpaSpecificationExecutor<SessionEvaluation> {

    // Détecte une session « en double » (même intitulé, même région, même début)
    boolean existsByIntituleIgnoreCaseAndRegionIgnoreCaseAndDateDebut(
            String intitule, String region, Instant dateDebut);
}