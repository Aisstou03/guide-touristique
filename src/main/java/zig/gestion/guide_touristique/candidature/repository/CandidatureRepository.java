package zig.gestion.guide_touristique.candidature.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import zig.gestion.guide_touristique.candidature.StatutCandidature;
import zig.gestion.guide_touristique.candidature.entity.Candidature;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    List<Candidature> findByGuideIdOrderByDateSoumissionDesc(Long guideId);

    // Unicité : ce guide a-t-il déjà une candidature « active » (≠ NON_ADMISE) sur cette session ?
    boolean existsByGuideIdAndSessionIdAndStatutNot(Long guideId, Long sessionId, StatutCandidature statut);

    // Capacité : nombre de candidatures actives de la session
    long countBySessionIdAndStatutNot(Long sessionId, StatutCandidature statut);
}