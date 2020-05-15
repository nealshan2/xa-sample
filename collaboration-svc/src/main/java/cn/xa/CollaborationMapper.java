package cn.xa;

import cn.xa.collaboration.CollaborationDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CollaborationMapper {

    public abstract CollaborationDto toDto(Collaboration entity);

    public abstract Collaboration toEntity(CollaborationDto dto);

}