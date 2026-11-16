package zig.gestion.guide_touristique.cv.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import zig.gestion.guide_touristique.cv.dto.CertificationDto;
import zig.gestion.guide_touristique.cv.dto.CvDto;
import zig.gestion.guide_touristique.cv.dto.CvSaisie;
import zig.gestion.guide_touristique.cv.dto.ExperienceDto;
import zig.gestion.guide_touristique.cv.dto.FormationDto;
import zig.gestion.guide_touristique.cv.dto.LangueDto;
import zig.gestion.guide_touristique.cv.entity.Certification;
import zig.gestion.guide_touristique.cv.entity.Cv;
import zig.gestion.guide_touristique.cv.entity.Experience;
import zig.gestion.guide_touristique.cv.entity.Formation;
import zig.gestion.guide_touristique.cv.entity.Langue;

@Mapper(componentModel = "spring")
public interface CvMapper {
    /** Entité -> DTO. L'id du guide vient de cv.getGuide().getId(). */
    @Mapping(target = "guideId", source = "guide.id")
    CvDto toDto(Cv cv);

    /** Recopie le contenu saisi dans le CV (version et date sont gérées par le service). */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "guide", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "dateMiseAJour", ignore = true)
    void updateEntity(CvSaisie saisie, @MappingTarget Cv cv);

    // Conversions des éléments, utilisées automatiquement pour les listes ci-dessus
    FormationDto toDto(Formation formation);
    Formation toEntity(FormationDto dto);

    ExperienceDto toDto(Experience experience);
    Experience toEntity(ExperienceDto dto);

    LangueDto toDto(Langue langue);
    Langue toEntity(LangueDto dto);

    CertificationDto toDto(Certification certification);
    Certification toEntity(CertificationDto dto);

}
