package zig.gestion.guide_touristique.evaluation.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import zig.gestion.guide_touristique.evaluation.DecisionEvaluation;
import zig.gestion.guide_touristique.evaluation.StatutPublication;

@Entity
@Table(name = "resultats_evaluation")
public class ResultatEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "candidature_id", nullable = false, unique = true)
    private Candidature candidature;

    @Column(nullable = false)
    private Long evaluateurId;

    @ElementCollection
    @CollectionTable(name = "resultat_notes", joinColumns = @JoinColumn(name = "resultat_id"))
    private List<NoteEvaluation> notes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DecisionEvaluation decision;

    @Column(length = 3000)
    private String appreciation;

    @Column(nullable = false)
    private Instant dateEvaluation;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal noteTotale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPublication statutPublication = StatutPublication.BROUILLON;

    public Long getId() { return id; }
    public Candidature getCandidature() { return candidature; }
    public void setCandidature(Candidature candidature) { this.candidature = candidature; }
    public Long getEvaluateurId() { return evaluateurId; }
    public void setEvaluateurId(Long evaluateurId) { this.evaluateurId = evaluateurId; }
    public List<NoteEvaluation> getNotes() { return notes; }
    public void setNotes(List<NoteEvaluation> notes) { this.notes = notes; }
    public DecisionEvaluation getDecision() { return decision; }
    public void setDecision(DecisionEvaluation decision) { this.decision = decision; }
    public String getAppreciation() { return appreciation; }
    public void setAppreciation(String appreciation) { this.appreciation = appreciation; }
    public Instant getDateEvaluation() { return dateEvaluation; }
    public void setDateEvaluation(Instant dateEvaluation) { this.dateEvaluation = dateEvaluation; }
    public BigDecimal getNoteTotale() { return noteTotale; }
    public void setNoteTotale(BigDecimal noteTotale) { this.noteTotale = noteTotale; }
    public StatutPublication getStatutPublication() { return statutPublication; }
    public void setStatutPublication(StatutPublication statutPublication) { this.statutPublication = statutPublication; }
}
