package zig.gestion.guide_touristique.evaluation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import zig.gestion.guide_touristique.evaluation.entity.ResultatEvaluation;

public interface ResultatEvaluationRepository extends JpaRepository<ResultatEvaluation, Long> {
    Optional<ResultatEvaluation> findByCandidatureId(Long candidatureId);
}
