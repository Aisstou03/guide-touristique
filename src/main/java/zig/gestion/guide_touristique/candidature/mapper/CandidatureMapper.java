package zig.gestion.guide_touristique.candidature.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import zig.gestion.guide_touristique.candidature.dto.CandidatureDto;
import zig.gestion.guide_touristique.candidature.entity.Candidature;

@Mapper(componentModel = "spring")
public interface CandidatureMapper {

    @Mapping(target = "guideId", source = "guide.id")
    @Mapping(target = "sessionId", source = "session.id")
    @Mapping(target = "versionCV", source = "cvFige.version")
    CandidatureDto toDto(Candidature candidature);
}