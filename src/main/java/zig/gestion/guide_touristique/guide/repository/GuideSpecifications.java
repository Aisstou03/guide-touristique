package zig.gestion.guide_touristique.guide.repository;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import zig.gestion.guide_touristique.cv.entity.Cv;
import zig.gestion.guide_touristique.cv.entity.Langue;
import zig.gestion.guide_touristique.guide.StatutGuide;
import zig.gestion.guide_touristique.guide.entity.Guide;

/** Construit une requête qui ne contient que les filtres réellement fournis. */
public final class GuideSpecifications {

    private GuideSpecifications() {
    }

    public static Specification<Guide> recherche(String nom, String langue, String region, StatutGuide statut) {
        return (root, query, cb) -> {
            List<Predicate> conditions = new ArrayList<>();

            if (StringUtils.hasText(nom)) {
                String motif = "%" + nom.trim().toLowerCase() + "%";
                conditions.add(cb.or(
                        cb.like(cb.lower(root.get("nom")), motif),
                        cb.like(cb.lower(root.get("prenom")), motif)));
            }
            if (StringUtils.hasText(region)) {
                conditions.add(cb.equal(cb.lower(root.get("region")), region.trim().toLowerCase()));
            }
            if (statut != null) {
                conditions.add(cb.equal(root.get("statut"), statut));
            }
            if (StringUtils.hasText(langue)) {
                // "Il existe un CV de ce guide qui contient cette langue"
                Subquery<Long> sousRequete = query.subquery(Long.class);
                Root<Cv> cv = sousRequete.from(Cv.class);
                Join<Cv, Langue> langues = cv.join("langues");
                sousRequete.select(cv.<Long>get("id")).where(
                        cb.equal(cv.get("guide"), root),
                        cb.equal(cb.lower(langues.<String>get("langue")), langue.trim().toLowerCase()));
                conditions.add(cb.exists(sousRequete));
            }
            return cb.and(conditions.toArray(new Predicate[0]));
        };
    }
}