package zig.gestion.guide_touristique.guide.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import zig.gestion.guide_touristique.guide.StatutGuide;
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

@Entity
@Table(name = "guides")
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, unique = true)   // un email = un seul guide
    private String email;

    private String telephone;

    private String photoUrl;

    @Column(nullable = false)
    private String region;

    private String ville;

    private String adresseProfessionnelle;

    // Liste de textes -> table séparée guide_specialites
    @ElementCollection
    @CollectionTable(name = "guide_specialites", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "specialite")
    private List<String> specialites = new ArrayList<>();

    @Enumerated(EnumType.STRING)               // stocké en texte ("BROUILLON"), pas en numéro
    @Column(nullable = false)
    private StatutGuide statut = StatutGuide.BROUILLON;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public String getAdresseProfessionnelle() { return adresseProfessionnelle; }
    public void setAdresseProfessionnelle(String adresseProfessionnelle) { this.adresseProfessionnelle = adresseProfessionnelle; }
    public List<String> getSpecialites() { return specialites; }
    public void setSpecialites(List<String> specialites) { this.specialites = specialites; }
    public StatutGuide getStatut() { return statut; }
    public void setStatut(StatutGuide statut) { this.statut = statut; }

}
