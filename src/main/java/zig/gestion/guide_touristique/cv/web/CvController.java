package zig.gestion.guide_touristique.cv.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zig.gestion.guide_touristique.cv.dto.CvDto;
import zig.gestion.guide_touristique.cv.dto.CvSaisie;
import zig.gestion.guide_touristique.cv.service.CvService;

@RestController
@RequestMapping("/api/v1/guides/{guideId}/cv")
public class CvController {

    private final CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    /** consulterCVGuide -> 200 ou 404 */
    @GetMapping
    public CvDto consulter(@PathVariable @Min(1) Long guideId) {
        return cvService.consulter(guideId);
    }

    /** enregistrerCVGuide -> 200 (création ou mise à jour) */
    @PutMapping
    public CvDto enregistrer(@PathVariable @Min(1) Long guideId, @Valid @RequestBody CvSaisie saisie) {
        return cvService.enregistrer(guideId, saisie);
    }

    /** exporterCVGuide -> 200 (application/pdf), 404 guide inconnu, 409 pas de CV */
    @PostMapping("/export")
    public ResponseEntity<byte[]> exporter(@PathVariable @Min(1) Long guideId) {
        byte[] pdf = cvService.exporter(guideId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("cv-guide-" + guideId + ".pdf").build().toString())
                .body(pdf);
    }
}