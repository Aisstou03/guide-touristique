package zig.gestion.guide_touristique.session.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import zig.gestion.guide_touristique.session.dto.SessionCreation;
import zig.gestion.guide_touristique.session.dto.SessionDto;
import zig.gestion.guide_touristique.session.entity.SessionEvaluation;

@Mapper(componentModel = "spring")
public interface SessionMapper {
     SessionDto toDto(SessionEvaluation session);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statut", ignore = true)
    SessionEvaluation toEntity(SessionCreation creation);

}
