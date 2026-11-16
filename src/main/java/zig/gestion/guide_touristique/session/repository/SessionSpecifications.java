package zig.gestion.guide_touristique.session.repository;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import zig.gestion.guide_touristique.session.entity.SessionEvaluation;
import zig.gestion.guide_touristique.session.StatutSession;

public final class SessionSpecifications {

    private SessionSpecifications() {
    }

    public static Specification<SessionEvaluation> recherche(String region, StatutSession statut) {
        return (root, query, cb) -> {
            List<Predicate> conditions = new ArrayList<>();
            if (StringUtils.hasText(region)) {
                conditions.add(cb.equal(cb.lower(root.get("region")), region.trim().toLowerCase()));
            }
            if (statut != null) {
                conditions.add(cb.equal(root.get("statut"), statut));
            }
            return cb.and(conditions.toArray(new Predicate[0]));
        };
    }
}