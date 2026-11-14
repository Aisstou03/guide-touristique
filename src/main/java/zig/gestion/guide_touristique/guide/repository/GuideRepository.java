package zig.gestion.guide_touristique.guide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import zig.gestion.guide_touristique.guide.entity.Guide;

public interface GuideRepository extends JpaRepository<Guide, Long>, JpaSpecificationExecutor<Guide> {
    // Spring Data génère la requête à partir du nom de la méthode
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

}
