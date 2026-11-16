package zig.gestion.guide_touristique.candidature.entity;

import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import zig.gestion.guide_touristique.candidature.StatutCandidature;
import zig.gestion.guide_touristique.guide.entity.Guide;
import zig.gestion.guide_touristique.session.entity.SessionEvaluation;

@Entity
@Table(name = "candidatures")
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private SessionEvaluation session;

    // La copie figée du CV appartient à la candidature : enregistrée et supprimée avec elle
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "cv_fige_id", nullable = false)
    private CvFige cvFige;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCandidature statut = StatutCandidature.SOUMISE;

    @Column(nullable = false)
    private Instant dateSoumission;

    @Column(length = 2000)
    private String commentaire;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Guide getGuide() { return guide; }
    public void setGuide(Guide guide) { this.guide = guide; }
    public SessionEvaluation getSession() { return session; }
    public void setSession(SessionEvaluation session) { this.session = session; }
    public CvFige getCvFige() { return cvFige; }
    public void setCvFige(CvFige cvFige) { this.cvFige = cvFige; }
    public StatutCandidature getStatut() { return statut; }
    public void setStatut(StatutCandidature statut) { this.statut = statut; }
    public Instant getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(Instant dateSoumission) { this.dateSoumission = dateSoumission; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}