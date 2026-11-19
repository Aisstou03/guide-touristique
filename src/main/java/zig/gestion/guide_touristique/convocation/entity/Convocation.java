package zig.gestion.guide_touristique.convocation.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import zig.gestion.guide_touristique.candidature.entity.Candidature;
import zig.gestion.guide_touristique.convocation.StatutConvocation;

@Entity
@Table(name = "convocations")
public class Convocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "candidature_id", nullable = false, unique = true)
    private Candidature candidature;

    @Column(nullable = false)
    private Instant dateHeure;

    @Column(nullable = false)
    private String lieu;

    @Column(length = 2000)
    private String instructions;

    private Instant dateLimiteConfirmation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutConvocation statut = StatutConvocation.ENVOYEE;

    public Long getId() { return id; }
    public Candidature getCandidature() { return candidature; }
    public void setCandidature(Candidature candidature) { this.candidature = candidature; }
    public Instant getDateHeure() { return dateHeure; }
    public void setDateHeure(Instant dateHeure) { this.dateHeure = dateHeure; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public Instant getDateLimiteConfirmation() { return dateLimiteConfirmation; }
    public void setDateLimiteConfirmation(Instant dateLimiteConfirmation) { this.dateLimiteConfirmation = dateLimiteConfirmation; }
    public StatutConvocation getStatut() { return statut; }
    public void setStatut(StatutConvocation statut) { this.statut = statut; }
}
