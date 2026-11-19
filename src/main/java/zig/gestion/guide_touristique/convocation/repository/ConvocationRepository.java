package zig.gestion.guide_touristique.convocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zig.gestion.guide_touristique.convocation.entity.Convocation;

public interface ConvocationRepository extends JpaRepository<Convocation, Long> {
    boolean existsByCandidatureId(Long candidatureId);
}
