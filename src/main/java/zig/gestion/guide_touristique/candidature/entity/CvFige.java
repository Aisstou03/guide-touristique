package zig.gestion.guide_touristique.candidature.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import zig.gestion.guide_touristique.cv.entity.Certification;
import zig.gestion.guide_touristique.cv.entity.Cv;
import zig.gestion.guide_touristique.cv.entity.Experience;
import zig.gestion.guide_touristique.cv.entity.Formation;
import zig.gestion.guide_touristique.cv.entity.Langue;

/** Copie figée d'un CV : elle ne change plus jamais après la soumission. */
@Entity
@Table(name = "cvs_figes")
public class CvFige {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int version;

    @Column(length = 200)
    private String titreProfessionnel;

    @Column(length = 3000)
    private String profilProfessionnel;

    @ElementCollection
    @CollectionTable(name = "cv_fige_formations", joinColumns = @JoinColumn(name = "cv_fige_id"))
    private List<Formation> formations = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cv_fige_experiences", joinColumns = @JoinColumn(name = "cv_fige_id"))
    private List<Experience> experiences = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cv_fige_langues", joinColumns = @JoinColumn(name = "cv_fige_id"))
    private List<Langue> langues = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cv_fige_competences", joinColumns = @JoinColumn(name = "cv_fige_id"))
    @Column(name = "competence")
    private List<String> competences = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cv_fige_certifications", joinColumns = @JoinColumn(name = "cv_fige_id"))
    private List<Certification> certifications = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "cv_fige_references_pro", joinColumns = @JoinColumn(name = "cv_fige_id"))
    @Column(name = "reference_pro")
    private List<String> referencesProfessionnelles = new ArrayList<>();

    @Column(nullable = false)
    private Instant dateMiseAJourOrigine;

    /** Fabrique la copie : de nouvelles listes, indépendantes de celles du CV d'origine. */
    public static CvFige depuis(Cv cv) {
        CvFige copie = new CvFige();
        copie.version = cv.getVersion();
        copie.titreProfessionnel = cv.getTitreProfessionnel();
        copie.profilProfessionnel = cv.getProfilProfessionnel();
        copie.formations = new ArrayList<>(cv.getFormations());
        copie.experiences = new ArrayList<>(cv.getExperiences());
        copie.langues = new ArrayList<>(cv.getLangues());
        copie.competences = new ArrayList<>(cv.getCompetences());
        copie.certifications = new ArrayList<>(cv.getCertifications());
        copie.referencesProfessionnelles = new ArrayList<>(cv.getReferencesProfessionnelles());
        copie.dateMiseAJourOrigine = cv.getDateMiseAJour();
        return copie;
    }

    public Long getId() { return id; }
    public int getVersion() { return version; }
    public String getTitreProfessionnel() { return titreProfessionnel; }
    public String getProfilProfessionnel() { return profilProfessionnel; }
    public List<Formation> getFormations() { return formations; }
    public List<Experience> getExperiences() { return experiences; }
    public List<Langue> getLangues() { return langues; }
    public List<String> getCompetences() { return competences; }
    public List<Certification> getCertifications() { return certifications; }
    public List<String> getReferencesProfessionnelles() { return referencesProfessionnelles; }
    public Instant getDateMiseAJourOrigine() { return dateMiseAJourOrigine; }
}