package zig.gestion.guide_touristique.cv.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.stereotype.Component;

import zig.gestion.guide_touristique.cv.entity.Certification;
import zig.gestion.guide_touristique.cv.entity.Cv;
import zig.gestion.guide_touristique.cv.entity.Experience;
import zig.gestion.guide_touristique.cv.entity.Formation;
import zig.gestion.guide_touristique.cv.entity.Langue;
import zig.gestion.guide_touristique.guide.entity.Guide;

/** Génère le CV d'un guide au format PDF. */
@Component
public class CvPdfGenerator {

    private static final Font TITRE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
    private static final Font SECTION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
    private static final Font TEXTE = FontFactory.getFont(FontFactory.HELVETICA, 10);

    public byte[] generer(Guide guide, Cv cv) {
        try (ByteArrayOutputStream sortie = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, sortie);
            document.open();

            // En-tête : identité et coordonnées
            document.add(new Paragraph(guide.getPrenom() + " " + guide.getNom(), TITRE));
            if (cv.getTitreProfessionnel() != null) {
                document.add(new Paragraph(cv.getTitreProfessionnel(), SECTION));
            }
            document.add(new Paragraph(guide.getEmail()
                    + (guide.getTelephone() != null ? " | " + guide.getTelephone() : "")
                    + " | " + guide.getRegion()
                    + (guide.getVille() != null ? ", " + guide.getVille() : ""), TEXTE));

            if (cv.getProfilProfessionnel() != null) {
                section(document, "Profil professionnel");
                document.add(new Paragraph(cv.getProfilProfessionnel(), TEXTE));
            }

            section(document, "Formations");
            for (Formation f : cv.getFormations()) {
                ligne(document, f.intitule() + " - " + f.etablissement() + " ("
                        + f.anneeDebut() + (f.anneeFin() != null ? "-" + f.anneeFin() : "") + ")"
                        + (f.diplomeObtenu() ? " [diplôme obtenu]" : ""));
            }

            section(document, "Expériences");
            for (Experience e : cv.getExperiences()) {
                ligne(document, e.poste() + " - " + e.organisme() + " (" + e.dateDebut() + " → "
                        + (e.enCours() || e.dateFin() == null ? "en cours" : e.dateFin()) + ")");
                if (e.description() != null) {
                    ligne(document, "   " + e.description());
                }
            }

            section(document, "Langues");
            for (Langue l : cv.getLangues()) {
                ligne(document, l.langue() + " : " + l.niveau());
            }

            liste(document, "Compétences", cv.getCompetences());

            if (!cv.getCertifications().isEmpty()) {
                section(document, "Certifications");
                for (Certification c : cv.getCertifications()) {
                    ligne(document, c.intitule() + " - " + c.organisme()
                            + (c.dateObtention() != null ? " (" + c.dateObtention() + ")" : ""));
                }
            }

            liste(document, "Références professionnelles", cv.getReferencesProfessionnelles());

            Paragraph pied = new Paragraph("CV version " + cv.getVersion(), TEXTE);
            pied.setAlignment(Element.ALIGN_RIGHT);
            pied.setSpacingBefore(15);
            document.add(pied);

            document.close();   // le document doit être fermé avant de lire les octets
            return sortie.toByteArray();
        } catch (DocumentException | IOException ex) {
            throw new IllegalStateException("Échec de la génération du PDF", ex);
        }
    }

    private void section(Document document, String titre) throws DocumentException {
        Paragraph p = new Paragraph(titre, SECTION);
        p.setSpacingBefore(12);
        document.add(p);
    }

    private void ligne(Document document, String texte) throws DocumentException {
        document.add(new Paragraph(texte, TEXTE));
    }

    private void liste(Document document, String titre, List<String> valeurs) throws DocumentException {
        if (valeurs.isEmpty()) {
            return;
        }
        section(document, titre);
        for (String v : valeurs) {
            ligne(document, "- " + v);
        }
    }
}