package zig.gestion.guide_touristique.cv.entity;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import zig.gestion.guide_touristique.guide.entity.Guide;

@Entity
@Table(name = "cvs")
public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Un CV appartient à un seul guide, et un guide a au plus un CV (unique = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guide_id", nullable = false, unique = true)
    private Guide guide;

    // Numéro de version : 1 au premier enregistrement, +1 à chaque PUT
    @Column(nullable = false)
    private int version;

    @Column(length = 200)
    private String titreProfessionnel;

    @Column(length = 3000)
    private String profilProfessionnel;

    @ElementCollection
    @CollectionTable(name = "cv_formations", joinColumns = @JoinColumn(name = "cv_id"))
    private List<Formation> formations = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cv_experiences", joinColumns = @JoinColumn(name = "cv_id"))
    private List<Experience> experiences = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cv_langues", joinColumns = @JoinColumn(name = "cv_id"))
    private List<Langue> langues = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cv_competences", joinColumns = @JoinColumn(name = "cv_id"))
    @Column(name = "competence")
    private List<String> competences = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cv_certifications", joinColumns = @JoinColumn(name = "cv_id"))
    private List<Certification> certifications = new ArrayList<>();

    // "references" est un mot réservé en SQL : on nomme la table et la colonne autrement
    @ElementCollection
    @CollectionTable(name = "cv_references_pro", joinColumns = @JoinColumn(name = "cv_id"))
    @Column(name = "reference_pro")
    private List<String> referencesProfessionnelles = new ArrayList<>();

    @Column(nullable = false)
    private Instant dateMiseAJour;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Guide getGuide() { return guide; }
    public void setGuide(Guide guide) { this.guide = guide; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public String getTitreProfessionnel() { return titreProfessionnel; }
    public void setTitreProfessionnel(String titreProfessionnel) { this.titreProfessionnel = titreProfessionnel; }
    public String getProfilProfessionnel() { return profilProfessionnel; }
    public void setProfilProfessionnel(String profilProfessionnel) { this.profilProfessionnel = profilProfessionnel; }
    public List<Formation> getFormations() { return formations; }
    public void setFormations(List<Formation> formations) { this.formations = formations; }
    public List<Experience> getExperiences() { return experiences; }
    public void setExperiences(List<Experience> experiences) { this.experiences = experiences; }
    public List<Langue> getLangues() { return langues; }
    public void setLangues(List<Langue> langues) { this.langues = langues; }
    public List<String> getCompetences() { return competences; }
    public void setCompetences(List<String> competences) { this.competences = competences; }
    public List<Certification> getCertifications() { return certifications; }
    public void setCertifications(List<Certification> certifications) { this.certifications = certifications; }
    public List<String> getReferencesProfessionnelles() { return referencesProfessionnelles; }
    public void setReferencesProfessionnelles(List<String> referencesProfessionnelles) { this.referencesProfessionnelles = referencesProfessionnelles; }
    public Instant getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(Instant dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }
}