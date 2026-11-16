package zig.gestion.guide_touristique.cv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import zig.gestion.guide_touristique.cv.entity.Cv;

public interface CvRepository extends JpaRepository<Cv, Long> {

    
    // Retrouve le CV d'un guide à partir de l'id du guide
    Optional<Cv> findByGuideId(Long guideId);

    // Supprime le CV d'un guide (utile quand on supprime le guide)
    void deleteByGuideId(Long guideId);
}