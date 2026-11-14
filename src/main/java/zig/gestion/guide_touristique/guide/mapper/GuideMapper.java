package zig.gestion.guide_touristique.guide.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import zig.gestion.guide_touristique.guide.dto.GuideCreation;
import zig.gestion.guide_touristique.guide.dto.GuideDto;
import zig.gestion.guide_touristique.guide.dto.GuideModification;
import zig.gestion.guide_touristique.guide.entity.Guide;

@Mapper(componentModel = "spring")
public interface GuideMapper {

    /** Entité -> DTO de réponse. */
    GuideDto toDto(Guide guide);

    /** DTO de création -> nouvelle entité (id, photo et statut ne viennent jamais du client). */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    @Mapping(target = "statut", ignore = true)
    Guide toEntity(GuideCreation creation);

    /** Applique les modifications sur une entité existante. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    @Mapping(target = "statut", ignore = true)
    void update(GuideModification modification, @MappingTarget Guide guide);

}
