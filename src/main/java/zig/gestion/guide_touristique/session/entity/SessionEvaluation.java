package zig.gestion.guide_touristique.session.entity;

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
import jakarta.persistence.Table;
import zig.gestion.guide_touristique.session.StatutSession;


@Entity
@Table(name = "sessions_evaluation")
public class SessionEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String intitule;

    @Column(nullable = false)
    private String region;

    private String lieu;

    @Column(nullable = false)
    private Instant dateDebut;

    @Column(nullable = false)
    private Instant dateFin;

    @Column(nullable = false)
    private int capacite;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "session_epreuves", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "epreuve")
    private List<String> epreuves = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSession statut = StatutSession.OUVERTE;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public Instant getDateDebut() { return dateDebut; }
    public void setDateDebut(Instant dateDebut) { this.dateDebut = dateDebut; }
    public Instant getDateFin() { return dateFin; }
    public void setDateFin(Instant dateFin) { this.dateFin = dateFin; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getEpreuves() { return epreuves; }
    public void setEpreuves(List<String> epreuves) { this.epreuves = epreuves; }
    public StatutSession getStatut() { return statut; }
    public void setStatut(StatutSession statut) { this.statut = statut; }
}